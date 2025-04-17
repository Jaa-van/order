package com.partridge.order.global.util;

import java.util.UUID;

public class KeyUtil {
	public static String generateKey() {
		return UUID.randomUUID().toString();
	}
}
