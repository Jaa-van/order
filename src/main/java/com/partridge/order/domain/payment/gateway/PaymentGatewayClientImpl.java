package com.partridge.order.domain.payment.gateway;

import org.springframework.stereotype.Component;

import com.partridge.order.domain.payment.dto.PaymentGatewayDTO;
import com.partridge.order.domain.payment.exception.PaymentGatewayFailException;
import com.partridge.order.global.constant.ConstantValue;

@Component
public class PaymentGatewayClientImpl implements PaymentGatewayClient{

	@Override
	public PaymentGatewayDTO.Response requestPayment(PaymentGatewayDTO.Request request) {
		return PaymentGatewayDTO.Response.builder()
			.status(ConstantValue.SUCCESS)
			.build();

		// if (Math.random() < 0.9) {
		// 	try {
		// 		Thread.sleep(200);
		// 		return PaymentGatewayDTO.Response.builder()
		// 			.status(ConstantValue.SUCCESS)
		// 			.build();
		// 	} catch (InterruptedException e) {
		// 		throw new PaymentGatewayFailException(request.getKey());
		// 	}
		// } else {
		// 	throw new PaymentGatewayFailException(request.getKey());
		// }
	}
}
