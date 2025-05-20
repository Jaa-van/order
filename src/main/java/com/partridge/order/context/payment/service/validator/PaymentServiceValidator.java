package com.partridge.order.context.payment.service.validator;

import static com.partridge.order.context.payment.constant.PaymentCommonCode.*;

import java.util.List;

import org.springframework.stereotype.Component;

import com.partridge.order.context.order.service.dto.OrderProductInventory;
import com.partridge.order.context.payment.exception.DuplicatePaymentException;
import com.partridge.order.context.payment.exception.PaymentAlreadyCompleteException;
import com.partridge.order.context.payment.exception.SoldOutException;
import com.partridge.order.context.payment.repository.PaymentRepository;
import com.partridge.order.context.payment.service.PaymentReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentServiceValidator {
	private final PaymentReader paymentReader;

	public void validateDuplicatePayment(Long orderId) {
		if (paymentReader.existsPaymentByOrderIdAndStatus(orderId, PAYMENT_IN_PROGRESS)) {
			throw new DuplicatePaymentException();
		}
		if (paymentReader.existsPaymentByOrderIdAndStatus(orderId, PAYMENT_COMPLETE)) {
			throw new PaymentAlreadyCompleteException();
		}
	}

	public void validateProductInventory(List<OrderProductInventory> inventoryList) {
		inventoryList.forEach(orderProductInventory -> {
				if (orderProductInventory.getQuantity() > orderProductInventory.getInventory()) {
					throw new SoldOutException(orderProductInventory.getName());
				}
			});
	}
}
