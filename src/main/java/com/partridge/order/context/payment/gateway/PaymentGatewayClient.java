package com.partridge.order.context.payment.gateway;

import com.partridge.order.context.payment.dto.PaymentGatewayDTO;

public interface PaymentGatewayClient {
	PaymentGatewayDTO.Response requestPayment(PaymentGatewayDTO.Request request);
}
