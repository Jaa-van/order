package com.partridge.order.domain.order.redis;

import static com.partridge.order.domain.order.constant.OrderCommonCode.*;
import static com.partridge.order.domain.order.constant.OrderConstantValue.*;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderRedisUtil {
	private final RedisTemplate<String, String> redisTemplate;
	private static int time = 10;
	private static String ORDER_KEY = "order:";

	public void setOrderWaiting(String key) {
		redisTemplate.opsForHash().put(convertOrderKey(key), CACHE_KEY_STATUS, ORDER_WAITING);
		redisTemplate.opsForHash().put(convertOrderKey(key), CACHE_KEY_EXPIRE_AT, expirationTime());
	}

	public void setOrderProgress(String key) {
		redisTemplate.opsForHash().put(convertOrderKey(key), CACHE_KEY_STATUS, ORDER_IN_PROGRESS);
		redisTemplate.opsForHash().put(convertOrderKey(key), CACHE_KEY_EXPIRE_AT, expirationTime());
	}

	public void setOrderComplete(String key) {
		redisTemplate.opsForHash().put(convertOrderKey(key), CACHE_KEY_STATUS, ORDER_COMPLETE);
		redisTemplate.opsForHash().put(convertOrderKey(key), CACHE_KEY_EXPIRE_AT, expirationTime());
	}

	public String getOrder(String key) {
		Map<Object, Object> hashValue = redisTemplate.opsForHash().entries(convertOrderKey(key));

		if (hashValue.isEmpty() || System.currentTimeMillis() > Long.parseLong((String)hashValue.get("expiresAt"))) {
			return ORDER_EXPIRED;
		}
		return hashValue.get(CACHE_KEY_STATUS).toString();
	}

	private String convertOrderKey(String key) {
		return ORDER_KEY + key;
	}

	private String expirationTime() {
		return String.valueOf(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(time));
	}
}
