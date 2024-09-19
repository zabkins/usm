package pl.zarczynski.usm.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@Builder
@Schema(description = "User information DTO")
public class UserInfoDto {
	@Schema(description = "User's full name", example = "John Doe")
	private final String name;
	@Schema(description = "User's email", example = "john.doe@gmail.com")
	private final String email;
	@Schema(description = "User's creation date", example = "30/01/2024 10:00:00 CEST")
	private final String createdAt;
	@Schema(description = "User's update date", example = "30/01/2024 10:30:00 CEST")
	private final String updatedAt;
	@Schema(description = "Is user account expired", example = "false")
	private final boolean isExpired;
	@Schema(description = "User's token expiration date", example = "30/01/2024 12:30:00 CEST. Valid for 3400s")
	private final String expiration;
}
