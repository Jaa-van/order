package com.partridge.order.domain.payment.validator;

import static com.partridge.order.domain.payment.constant.PaymentCommonCode.*;

import java.util.List;

import org.springframework.stereotype.Component;

import com.partridge.order.domain.order.repository.OrderRepository;
import com.partridge.order.domain.payment.dto.PaymentPostDTO;
import com.partridge.order.domain.payment.exception.DuplicatePaymentException;
import com.partridge.order.domain.payment.exception.PaymentAlreadyCompleteException;
import com.partridge.order.domain.payment.exception.SoldOutException;
import com.partridge.order.domain.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentValidator {
	private final PaymentRepository paymentRepository;

	public void validateDuplicatePayment(Long orderId) {
		if (paymentRepository.existsPaymentByOrderId(orderId, PAYMENT_IN_PROGRESS)) {
			throw new DuplicatePaymentException();
		}
		if (paymentRepository.existsPaymentByOrderId(orderId, PAYMENT_COMPLETE)) {
			throw new PaymentAlreadyCompleteException();
		}
	}

	public void validateProductInventory(List<PaymentPostDTO.OrderProductInventory> inventoryList) {
		inventoryList.forEach(orderProductInventory -> {
				if (orderProductInventory.getQuantity() > orderProductInventory.getInventory()) {
					throw new SoldOutException(orderProductInventory.getName());
				}
			});
	}
}
