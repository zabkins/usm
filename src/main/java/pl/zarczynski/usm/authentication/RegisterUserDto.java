package pl.zarczynski.usm.authentication;

import lombok.Data;
import lombok.ToString;

@Data
public class RegisterUserDto {
	private String email;
	@ToString.Exclude
	private String password;
	private String fullName;
}
