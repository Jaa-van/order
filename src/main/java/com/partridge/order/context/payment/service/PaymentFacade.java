package com.partridge.order.context.payment.service;

import static com.partridge.order.context.payment.constant.PaymentCommonCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.partridge.order.context.order.infra.OrderRedisUtil;
import com.partridge.order.context.order.service.OrderReader;
import com.partridge.order.context.order.service.OrderWriter;
import com.partridge.order.context.order.service.dto.OrderProductInventory;
import com.partridge.order.context.payment.controller.dto.PaymentPostDTO;
import com.partridge.order.context.payment.domain.model.Payment;
import com.partridge.order.context.payment.exception.PaymentGatewayFailException;
import com.partridge.order.context.payment.infra.dto.PaymentGatewayMapper;
import com.partridge.order.context.payment.infra.payment.PaymentGatewayClient;
import com.partridge.order.context.payment.service.dto.PaymentServiceMapper;
import com.partridge.order.context.payment.service.validator.PaymentServiceValidator;
import com.partridge.order.context.product.service.ProductWriter;
import com.partridge.order.global.logger.Log;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentFacade {
	private final OrderReader orderReader;
	private final OrderWriter orderWriter;
	private final PaymentWriter paymentWriter;
	private final ProductWriter productWriter;
	private final OrderRedisUtil orderRedisUtil;
	private final PaymentServiceValidator paymentServiceValidator;
	private final PaymentGatewayClient paymentGatewayClient;

	private final PaymentServiceMapper paymentServiceMapper;
	private final PaymentGatewayMapper paymentGatewayMapper;

	@Log
	@Transactional(rollbackFor = Exception.class)
	// @AcquireLock(lockName = PAYMENT_LOCK_NAME)
	// @ReleaseLock(lockName = PAYMENT_LOCK_NAME)
	public Payment postPayment(PaymentPostDTO.Request request) {
		Long orderId = request.getOrderId();
		List<OrderProductInventory> productInventoryList = orderReader.getProductInventoryListByOrderId(
			request.getOrderId());
		paymentServiceValidator.validateProductInventory(productInventoryList);
		paymentServiceValidator.validateDuplicatePayment(orderId);

		Payment payment;
		try {
			payment = paymentWriter.postPayment(paymentServiceMapper.toEntityWithPaymentComplete(request));
			orderWriter.updateOrderStatusByOrderId(orderId, PAYMENT_COMPLETE);
			orderRedisUtil.setOrderComplete(request.getKey());
			productInventoryList.forEach(
				productInventory -> productWriter.updateProductInventory(productInventory.getProductId(),
					productInventory.getQuantity()));

			paymentGatewayClient.requestPayment(
				paymentGatewayMapper.toRequest(request, orderReader.getTotalPriceByOrderId(orderId)));
		} catch (PaymentGatewayFailException e) {
			paymentWriter.postPayment(paymentServiceMapper.toEntityWithPaymentFailed(request));
			orderRedisUtil.setOrderWaiting(request.getKey());

			throw new PaymentGatewayFailException(e.getMessage());
		}

		return payment;
	}
}
