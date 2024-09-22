package pl.zarczynski.usm.swaggerschemas.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Api Error details")
@Getter
public abstract class BadCredentialsProblemDetailSchema {
	@Schema(name = "type", example = "about:blank")
	private String type;
	@Schema(name = "title", example = "Unauthorized")
	private String title;
	@Schema(name = "status", example = "401")
	private Integer status;
	@Schema(name = "detail", example = "Bad Credentials")
	private String detail;
	@Schema(name = "instance", example = "/auth/login")
	private String instance;
	@Schema(name = "description", example = "The username or password is incorrect")
	private String description;
}