package com.partridge.order.domain.auth.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class LoginDTO {
	@Getter
	@ToString
	@EqualsAndHashCode
	public static class Request {
		private final String email;
		private final String password;

		public Request(String email, String password) {
			this.email = email;
			this.password = password;
		}
	}

	@Getter
	@Builder
	@ToString
	@EqualsAndHashCode
	public static class Response {
		private final String token;
	}
}
