package com.partridge.order.domain.payment.gateway;

import com.partridge.order.domain.payment.dto.PaymentGatewayDTO;

public interface PaymentGatewayClient {
	PaymentGatewayDTO.Response requestPayment(PaymentGatewayDTO.Request request);
}
