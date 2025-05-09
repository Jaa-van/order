package com.partridge.order.context.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.partridge.order.context.auth.dto.LoginDTO;
import com.partridge.order.context.auth.repository.AuthRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	@InjectMocks
	private AuthService authService;

	@Mock
	private AuthRepository authRepository;

	@Test
	void login_should_return_successful_response_when_email_and_password_is_valid() {
		//given
		String email = "test@test.com";
		String password = "password";
		LoginDTO.Request request = new LoginDTO.Request(email, password);

		when(authRepository.existsByEmailAndPassword(email, password)).thenReturn(true);

		// when
		LoginDTO.Response response = authService.login(request);

		// then
		assertThat(response.getToken()).isEqualTo("success");
	}

	@Test
	void login_should_return_fail_response_when_email_and_password_is_not_valid() {
		//given
		String email = "test@test.com";
		String password = "password";
		LoginDTO.Request request = new LoginDTO.Request(email, password);

		when(authRepository.existsByEmailAndPassword(email, password)).thenReturn(false);

		// when
		LoginDTO.Response response = authService.login(request);

		// then
		assertThat(response.getToken()).isEqualTo("fail");
	}
}