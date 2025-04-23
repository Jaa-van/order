package com.partridge.order.global.lock;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.partridge.order.global.lock.redis.LockRedisUtil;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LockAspect {
	private final LockRedisUtil lockRedisUtil;

	@Before("@annotation(acquireLock)")
	public void acquireLock(JoinPoint joinPoint, AcquireLock acquireLock) {
		lockRedisUtil.getLock(acquireLock.lockName());
	}

	// @After("@annotation(releaseLock)")
	// public void releaseLock(JoinPoint joinPoint, ReleaseLock releaseLock) {
	// 	lockRedisUtil.releaseLock(releaseLock.lockName());
	// }

	// @Before("@annotation(releaseLock)")
	// public void releaseLock(JoinPoint joinPoint, ReleaseLock releaseLock) {
	// 	String lockName = releaseLock.lockName();
	//
	// 	if (TransactionSynchronizationManager.isSynchronizationActive()) {
	// 		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
	// 			@Override
	// 			public void afterCommit() {
	// 				lockRedisUtil.releaseLock(lockName);
	// 			}
	// 		});
	// 	} else {
	// 		lockRedisUtil.releaseLock(lockName);
	// 	}
	// }

	@Before("@annotation(releaseLock)")
	public void releaseLock(JoinPoint joinPoint, ReleaseLock releaseLock) {
		String lockName = releaseLock.lockName();

		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCompletion(int status) {
					lockRedisUtil.releaseLock(lockName);
				}
			});
		} else {
			lockRedisUtil.releaseLock(lockName);
		}
	}
}
