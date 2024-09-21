package pl.zarczynski.usm.swagger.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Api Error details")
@Getter
public abstract class InvalidEmailProblemDetailSchema {
	@Schema(name = "type", example = "about:blank")
	private String type;
	@Schema(name = "title", example = "Unauthorized")
	private String title;
	@Schema(name = "status", example = "401")
	private Integer status;
	@Schema(name = "detail", example = "Invalid email format")
	private String detail;
	@Schema(name = "instance", example = "/auth/signup")
	private String instance;
	@Schema(name = "description", example = "The username or password is incorrect")
	private String description;
}