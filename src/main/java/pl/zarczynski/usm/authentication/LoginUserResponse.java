package pl.zarczynski.usm.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginUserResponse {
	private final String token;
	private final long expiresIn;
}
