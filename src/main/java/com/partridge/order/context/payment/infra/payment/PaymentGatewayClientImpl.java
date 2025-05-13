package com.partridge.order.context.payment.infra.payment;

import org.springframework.stereotype.Component;

import com.partridge.order.context.payment.controller.dto.PaymentGatewayDTO;
import com.partridge.order.context.payment.exception.PaymentGatewayFailException;
import com.partridge.order.global.constant.ConstantValue;

@Component
public class PaymentGatewayClientImpl implements PaymentGatewayClient{

	@Override
	public PaymentGatewayDTO.Response requestPayment(PaymentGatewayDTO.Request request) {
		if ((int) (Math.random()*100) + 1 < 90) {
			try {
				Thread.sleep(100);
				return PaymentGatewayDTO.Response.builder()
					.status(ConstantValue.SUCCESS)
					.build();
			} catch (InterruptedException e) {
				throw new PaymentGatewayFailException(request.getKey());
			}
		} else {
			throw new PaymentGatewayFailException(request.getKey());
		}
	}
}
