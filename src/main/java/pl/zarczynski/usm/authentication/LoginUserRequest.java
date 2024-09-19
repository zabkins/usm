package pl.zarczynski.usm.authentication;

import lombok.Data;

@Data
public class LoginUserRequest {
	private String email;
	private String password;
}
