package pl.zarczynski.usm.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.configuration.user.User;


@Getter
@EqualsAndHashCode
@ToString
@Schema(description = "Register user response")
public class RegisterUserResponse {
	@Schema(description = "User's ID", name = "id", example = "1")
	private final Integer id;
	@Schema(description = "User's full name", name = "fullName", example = "John Doe")
	private final String fullName;
	@Schema(description = "User's email", name = "email", example = "john.doe@gmail.com")
	private final String email;
	@Schema(description = "User's creation date", name = "createdAt", example = "30/01/2024 10:00:00 CEST")
	private final String createdAt;
	@Schema(description = "User's update date", name = "updatedAt", example = "30/01/2024 10:30:00 CEST")
	private final String updatedAt;

	public RegisterUserResponse(User user) {
		this.id = user.getId();
		this.fullName = user.getFullName();
		this.email = user.getEmail();
		this.createdAt = DateHelper.parseDate(user.getCreatedAt());
		this.updatedAt = DateHelper.parseDate(user.getUpdatedAt());
	}
}
