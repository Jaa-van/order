package com.partridge.order.domain.order.service;

import static com.partridge.order.domain.order.constant.OrderCommonCode.*;
import static com.partridge.order.domain.order.constant.OrderConstantValue.*;
import static com.partridge.order.global.util.DeliveryUtil.*;
import static com.partridge.order.global.util.KeyUtil.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.domain.order.dto.OrderPostDTO;
import com.partridge.order.domain.order.exception.DuplicateOrderRequestException;
import com.partridge.order.domain.order.exception.InventoryNotEnoughException;
import com.partridge.order.domain.order.exception.OrderExpiredException;
import com.partridge.order.domain.order.redis.OrderRedisUtil;
import com.partridge.order.domain.order.repository.OrderProductRepotisory;
import com.partridge.order.domain.order.repository.OrderRepository;
import com.partridge.order.domain.product.repository.ProductRepository;
import com.partridge.order.global.entity.Order;
import com.partridge.order.global.entity.OrderProduct;
import com.partridge.order.global.entity.OrderProductId;
import com.partridge.order.global.entity.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final OrderProductRepotisory orderProductRepotisory;
	private final OrderRedisUtil redisUtil;

	public String postOrderKey() {
		String key = generateKey();
		redisUtil.putOrderKey(ORDER_KEY + key, ORDER_WAITING, ORDER_KEY_TIMEOUT, TimeUnit.MINUTES);

		return key;
	}

	@Transactional
	public OrderPostDTO.Response postOrder(OrderPostDTO.Request request) {
		switch (redisUtil.getOrderKey(ORDER_KEY + request.getKey())) {
			case ORDER_IN_PROGRESS:
			case ORDER_COMPLETE:
				throw new DuplicateOrderRequestException();
			case ORDER_EXPIRED:
				throw new OrderExpiredException();
		}

		Order order = orderRepository.save(postRequestToEntity(request,
			getProductInformationMap(productRepository.findAllById(request.getProducts().stream()
				.map(OrderPostDTO.RequestProduct::getProductId).toList()))));

		request.getProducts().stream()
			.map(product -> orderProductBuilder(order.getId(), product.getProductId(), product.getQuantity()))
			.forEach(orderProductRepotisory::save);

		redisUtil.putOrderKey(ORDER_KEY + request.getKey(), ORDER_IN_PROGRESS, ORDER_KEY_TIMEOUT, TimeUnit.MINUTES);

		return postResponseBuilder(order);
	}

	private Order postRequestToEntity(OrderPostDTO.Request request, Map<Long, OrderPostDTO.ProductInformation> productInformationMap) {
		Long totalPrice = request.getProducts()
			.stream()
			.mapToLong(product -> {
				if (product.getQuantity() > productInformationMap.get(product.getProductId()).getInventory()) {
					throw new InventoryNotEnoughException(productInformationMap.get(product.getProductId()).getName());
				} else {
					return productInformationMap.get(product.getProductId()).getPrice() * product.getQuantity();
				}
			})
			.sum();

		return Order.builder()
			.key(request.getKey())
			.userId(request.getUserId())
			.totalPrice(totalPrice)
			.deliveryFee(deliveryFeeYn(totalPrice))
			.status(ORDER_IN_PROGRESS)
			.build();
	}

	private Map<Long, OrderPostDTO.ProductInformation> getProductInformationMap(List<Product> productList) {
		return productList.stream()
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

	private OrderProduct orderProductBuilder(Long orderId, Long productId, Long quantity) {
		return OrderProduct.builder()
			.id(OrderProductId.builder()
				.orderId(orderId)
				.productId(productId)
				.build())
			.quantity(quantity)
			.build();
	}

}
