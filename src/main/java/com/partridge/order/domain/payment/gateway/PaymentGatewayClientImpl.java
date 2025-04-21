package com.partridge.order.domain.payment.gateway;

import org.springframework.stereotype.Component;

import com.partridge.order.domain.payment.dto.PaymentGatewayDTO;
import com.partridge.order.domain.payment.exception.PaymentGatewayFailException;
import com.partridge.order.global.constant.ConstantValue;

@Component
public class PaymentGatewayClientImpl implements PaymentGatewayClient{

	@Override
	public PaymentGatewayDTO.Response requestPayment(PaymentGatewayDTO.Request request) {

		if (Math.random() < 0.9) {
			return PaymentGatewayDTO.Response.builder()
				.status(ConstantValue.SUCCESS)
				.build();
		} else {
			throw new PaymentGatewayFailException(request.getKey());
		}

	}
}
