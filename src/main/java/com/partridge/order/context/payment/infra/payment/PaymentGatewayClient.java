package com.partridge.order.context.payment.infra.payment;

import com.partridge.order.context.payment.infra.dto.PaymentGatewayDTO;

public interface PaymentGatewayClient {
	PaymentGatewayDTO.Response requestPayment(PaymentGatewayDTO.Request request);
}
