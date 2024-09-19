package pl.zarczynski.usm.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "User login response")
public class LoginUserResponse {
	@Schema(description = "Access token", name = "token", example = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTcyNjczODM4MCwiZXhwIjoxNzI2NzQxOTgwfQ.1-PnIbSgqiZfHSBOPtbfPvbuT58O88v03RqCwXMgC5X0N8OM-WvVKSQGIyI3aeCr")
	private final String token;
	@Schema(description = "Access token expiration", name = "expiresIn", example = "3600000")
	private final long expiresIn;
}
