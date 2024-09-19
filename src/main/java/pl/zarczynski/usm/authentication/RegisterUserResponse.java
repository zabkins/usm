package pl.zarczynski.usm.authentication;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.configuration.user.User;


@Getter
@EqualsAndHashCode
@ToString
public class RegisterUserResponse {
	private final Integer id;
	private final String fullName;
	private final String email;
	private final String createdAt;
	private final String updatedAt;

	public RegisterUserResponse(User user) {
		this.id = user.getId();
		this.fullName = user.getFullName();
		this.email = user.getEmail();
		this.createdAt = DateHelper.parseDate(user.getCreatedAt());
		this.updatedAt = DateHelper.parseDate(user.getUpdatedAt());
	}
}
