package com.partridge.order.global.logger;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerAspect {
	private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

	@Before("execution(* com.partridge.order..*Service.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		logger.info("Calling method: {} with args: {}", methodName, Arrays.toString(args));
	}

	@Around("execution(* com.partridge.order..*Service.*(..))")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		try {
			return joinPoint.proceed();
		} finally {
			long duration = System.currentTimeMillis() - start;
			logger.info("Executed {} in {} ms", joinPoint.getSignature(), duration);
		}
	}

	@AfterReturning(value = "execution(* com.partridge.order..*Service.*(..))", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		logger.info("{}: {}", joinPoint.getSignature().getName(), result);
	}

	@AfterThrowing(value = "execution(* com.partridge.order..*Service.*(..))", throwing = "exception")
	public void logAfterThrowing(Exception exception) {
		logger.error("{}", exception.getMessage());
	}
}
