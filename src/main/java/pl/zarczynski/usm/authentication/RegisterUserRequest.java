package pl.zarczynski.usm.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@Schema(description = "DTO Used for user registration")
public class RegisterUserRequest {
	@Schema(description = "User's email address", name = "email", example = "john.doe@gmail.com")
	private String email;
	@ToString.Exclude
	@Schema(description = "User's password", name = "password", example = "johnDoe123")
	private String password;
	@Schema(description = "User's full name", name = "fullName", example = "John Doe")
	private String fullName;
}
