package com.partridge.order.context.payment.controller.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.partridge.order.context.payment.domain.model.Payment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentResponseMapper {

	@Mapping(target = "paymentId", source = "id")
	public PaymentPostDTO.Response toResponse(Payment payment);
}
