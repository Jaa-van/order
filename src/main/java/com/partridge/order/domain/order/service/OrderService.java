package com.partridge.order.domain.order.service;

import static com.partridge.order.domain.order.constant.OrderCommonCode.*;
import static com.partridge.order.global.util.DeliveryUtil.*;
import static com.partridge.order.global.util.KeyUtil.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.domain.order.dto.OrderPostDTO;
import com.partridge.order.domain.order.dto.OrderPostKeyDTO;
import com.partridge.order.domain.order.redis.OrderRedisUtil;
import com.partridge.order.domain.order.repository.OrderProductRepotisory;
import com.partridge.order.domain.order.repository.OrderRepository;
import com.partridge.order.domain.order.validator.OrderValidator;
import com.partridge.order.domain.product.repository.ProductRepository;
import com.partridge.order.global.entity.Order;
import com.partridge.order.global.entity.OrderProduct;
import com.partridge.order.global.entity.OrderProductId;
import com.partridge.order.global.entity.Product;
import com.partridge.order.global.logger.Log;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final OrderProductRepotisory orderProductRepotisory;
	private final OrderRedisUtil orderRedisUtil;
	private final OrderValidator orderValidator;

	@Log
	public OrderPostKeyDTO.Resposne postOrderKey() {
		String key = generateKey();
		orderRedisUtil.setOrderWaiting(key);
		return postKeyResponseBuilder(key);
	}

	@Log
	@Transactional(rollbackFor = Exception.class)
	public OrderPostDTO.Response postOrder(OrderPostDTO.Request request) {
		orderValidator.validateOrderKey(request.getKey());
		Map<Long, OrderPostDTO.ProductInformation> productInformationMap = getProductInformationMap(request);
		orderValidator.validateProductInventory(request, productInformationMap);

		Order order = orderRepository.save(postRequestToEntity(request, productInformationMap));
		postOrderProduct(request, order.getId());

		orderRedisUtil.setOrderProgress(request.getKey());

		return postResponseBuilder(order);
	}

	private OrderPostKeyDTO.Resposne postKeyResponseBuilder(String key) {
		return OrderPostKeyDTO.Resposne.builder()
			.key(key)
			.build();
	}

	private void postOrderProduct(OrderPostDTO.Request request, Long orderId) {
		request.getProducts().stream()
			.map(product -> orderProductBuilder(orderId, product))
			.forEach(orderProductRepotisory::save);
	}

	private Order postRequestToEntity(OrderPostDTO.Request request,
		Map<Long, OrderPostDTO.ProductInformation> productInformationMap) {
		Long totalPrice = request.getProducts().stream()
			.mapToLong(product -> productInformationMap.get(product.getProductId()).getPrice() * product.getQuantity())
			.sum();
		return Order.builder()
			.key(request.getKey())
			.userId(request.getUserId())
			.totalPrice(totalPrice)
			.deliveryFee(deliveryFeeYn(totalPrice))
			.status(ORDER_IN_PROGRESS)
			.build();
	}

	private Map<Long, OrderPostDTO.ProductInformation> getProductInformationMap(OrderPostDTO.Request request) {
		List<Product> products = productRepository.findAllById(request.getProducts().stream()
			.map(OrderPostDTO.RequestProduct::getProductId).toList());
		return products.stream()
			.collect(Collectors.toMap(Product::getId,
				product ->
					OrderPostDTO.ProductInformation.builder()
						.productId(product.getId())
						.price(product.getPrice())
						.inventory(product.getInventory())
						.name(product.getName())
						.build()));
	}

	private OrderPostDTO.Response postResponseBuilder(Order order) {
		return OrderPostDTO.Response.builder()
			.orderId(order.getId())
			.key(order.getKey())
			.totalPrice(order.getTotalPrice())
			.deliveryFee(caculateDeliveryFee(order.getDeliveryFee()))
			.status(order.getStatus())
			.build();
	}

	private OrderProduct orderProductBuilder(Long orderId, OrderPostDTO.RequestProduct product) {
		return OrderProduct.builder()
			.id(OrderProductId.builder()
				.orderId(orderId)
				.productId(product.getProductId())
				.build())
			.quantity(product.getQuantity())
			.build();
	}

}
