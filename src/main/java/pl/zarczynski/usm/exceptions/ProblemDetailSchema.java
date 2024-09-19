package pl.zarczynski.usm.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Api Error details")
@Getter
@Setter
public abstract class ProblemDetailSchema {
	@Schema(name = "type", example = "about:blank")
	private String type;
	@Schema(name = "title", example = "Unauthorized")
	private String title;
	@Schema(name = "status", example = "401")
	private Integer status;
	@Schema(name = "detail", example = "Email is already in use")
	private String detail;
	@Schema(name = "instance", example = "/auth/signup")
	private String instance;
	@Schema(name = "description", example = "The username or password is incorrect")
	private String description;
}
