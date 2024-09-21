package pl.zarczynski.usm.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User's login credentials")
public class LoginUserRequest {
	@Schema(description = "User's email address", name = "email", example = "john.doe@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
	private String email;
	@Schema(description = "User's password", name = "password", example = "johnDoe123", requiredMode = Schema.RequiredMode.REQUIRED)
	private String password;
}
