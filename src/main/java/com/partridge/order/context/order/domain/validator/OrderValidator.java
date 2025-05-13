package com.partridge.order.context.order.domain.validator;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.partridge.order.context.order.controller.dto.OrderPostDto;
import com.partridge.order.context.order.exception.InventoryNotEnoughException;
import com.partridge.order.context.product.service.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderValidator {

}
