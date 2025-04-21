package com.partridge.order.domain.order.redis;

import static com.partridge.order.domain.order.constant.OrderCommonCode.*;

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

	public void putOrderKey(String key, String value, int seconds, TimeUnit timeUnit) {
		long expirationTime = System.currentTimeMillis() + timeUnit.toMillis(seconds);
		redisTemplate.opsForHash().put(key, "status", value);
		redisTemplate.opsForHash().put(key, "expiresAt", expirationTime);
	}

	public String getOrderKey(String key) {
		Map<Object, Object> hashValue = redisTemplate.opsForHash().entries(key);
		if (System.currentTimeMillis() > Long.parseLong((String)hashValue.get("expiresAt"))) {
			return ORDER_EXPIRED;
		}
		return hashValue.get("status").toString();
	}
}
