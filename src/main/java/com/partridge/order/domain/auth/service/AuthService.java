package com.partridge.order.domain.auth.service;

import static com.partridge.order.global.constant.ConstantValue.*;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.partridge.order.domain.auth.dto.LoginDTO;
import com.partridge.order.domain.auth.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final RedisTemplate<String, String> redisTemplate;
	private final AuthRepository authRepository;

	public LoginDTO.Response login(LoginDTO.Request request) {
		return Optional.of(request)
			.filter(req -> authRepository.existsByEmailAndPassword(req.getEmail(), req.getPassword()))
			.map(req -> LoginDTO.Response.builder().token(SUCCESS).build())
			.orElse(LoginDTO.Response.builder().token(FAIL).build());
	}
}
