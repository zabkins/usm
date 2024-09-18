package pl.zarczynski.usm.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
	private String token;
	private long expiresIn;
}
