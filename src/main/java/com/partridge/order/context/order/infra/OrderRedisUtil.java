package com.partridge.order.context.order.infra;

import static com.partridge.order.context.order.constant.OrderCommonCodeEnum.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderRedisUtil {
	private final RedisTemplate<String, String> redisTemplate;
	private static int time = 10;
	private static final String ORDER_KEY = "order:";
	public static final String CACHE_KEY_STATUS = "status";
	public static final String CACHE_KEY_EXPIRE_AT = "expiresAt";

	public void setOrderWaiting(String key) {
		redisTemplate.opsForHash().put(convertOrderKey(key), CACHE_KEY_STATUS, ORDER_WAITING.getCode());
		redisTemplate.opsForHash().put(convertOrderKey(key), CACHE_KEY_EXPIRE_AT, expirationTime());
	}

	public void setOrderProgress(String key) {
		redisTemplate.opsForHash().put(convertOrderKey(key), CACHE_KEY_STATUS, ORDER_IN_PROGRESS.getCode());
		redisTemplate.opsForHash().put(convertOrderKey(key), CACHE_KEY_EXPIRE_AT, expirationTime());
	}

	public void setOrderComplete(String key) {
		redisTemplate.opsForHash().put(convertOrderKey(key), CACHE_KEY_STATUS, ORDER_COMPLETE.getCode());
		redisTemplate.opsForHash().put(convertOrderKey(key), CACHE_KEY_EXPIRE_AT, expirationTime());
	}

	public String getOrder(String key) {
		Map<Object, Object> hashValue = redisTemplate.opsForHash().entries(convertOrderKey(key));

		if (hashValue.isEmpty() || System.currentTimeMillis() > Long.parseLong(
			(String)hashValue.get(CACHE_KEY_EXPIRE_AT))) {
			return ORDER_EXPIRED.getCode();
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
