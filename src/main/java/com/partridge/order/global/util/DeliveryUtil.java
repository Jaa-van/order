package com.partridge.order.global.util;

public class DeliveryUtil {
	private static final Long deliveryFee = 5000L;
	private static final Long freeDeliveryLimit = 50000L;

	public static Long caculateDeliveryFee(boolean deliveryFeeYn) {
		return deliveryFeeYn ? deliveryFee : 0L;
	}

	public static boolean deliveryFeeYn(Long totalPrice) {
		return totalPrice > freeDeliveryLimit;
	}
}
