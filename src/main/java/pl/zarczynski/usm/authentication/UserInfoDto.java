package pl.zarczynski.usm.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {
	private String name;
	private String email;
	private String createdAt;
	private boolean isExpired;
	private String expiration;
}
