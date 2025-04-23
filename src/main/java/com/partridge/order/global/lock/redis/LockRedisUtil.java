package com.partridge.order.global.lock.redis;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.partridge.order.global.lock.exception.AcquireLockTimeOutException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LockRedisUtil {
	private final RedisTemplate<String, String> redisTemplate;
	private static String LOCK_STATUS = "status";
	private static String LOCKED = "001";
	private static String UNLOCKED = "002";

	private static final ThreadLocal<Integer> tryCount = ThreadLocal.withInitial(() -> 0);

	// public void getLock(String lockName) {
	// 	String status = redisTemplate.opsForValue().get(lockName);
	// 	if (status == null || status.equals(UNLOCKED)) {
	// 		redisTemplate.opsForValue().set(lockName, LOCKED);
	// 		log.info("Get lock thread: {}", Thread.currentThread().getName());
	// 		return;
	// 	}
	//
	// 	while (status.equals(LOCKED)) {
	// 		log.info("Acquire Lock status: {}", status + " " + Thread.currentThread().getName() + " " + tryCount.get().toString());
	//
	// 		if (tryCount.get() > 20) {
	// 			throw new AcquireLockTimeOutException(lockName);
	// 		}
	// 		tryCount.set(tryCount.get() + 1);
	// 		try {
	// 			Thread.sleep(300);
	// 		} catch (InterruptedException e) {
	// 			throw new RuntimeException(e);
	// 		}
	// 		status = redisTemplate.opsForValue().get(lockName);
	// 	}
	// 	redisTemplate.opsForValue().set(lockName, LOCKED);
	// 	log.info("Get lock thread: {}", Thread.currentThread().getName());
	// }

	public void getLock(String lockName) {
		int maxRetries = 30;
		int retryCount = 0;
		log.info("Try to get lock thread: {}", Thread.currentThread().getName());
		while (retryCount < maxRetries) {
			if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockName, LOCKED, 10, TimeUnit.SECONDS))) {
				return;
			}

			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException("Lock acquisition interrupted", e);
			}

			retryCount++;
		}

		throw new AcquireLockTimeOutException(lockName);
	}

	public void releaseLock(String lockName) {
		redisTemplate.delete(lockName);
	}
}
