package pl.zarczynski.usm.swaggerschemas.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Api Error details")
@Getter
public abstract class TokenRefreshProblemDetailSchema {
	@Schema(name = "type", example = "about:blank")
	private String type;
	@Schema(name = "title", example = "Unauthorized")
	private String title;
	@Schema(name = "status", example = "401")
	private Integer status;
	@Schema(name = "detail", example = "Invalid JWT Token")
	private String detail;
	@Schema(name = "instance", example = "/auth/signup")
	private String instance;
	@Schema(name = "description", example = "JWT provided in the request is invalid")
	private String description;
}