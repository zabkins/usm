package pl.zarczynski.usm.swagger.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Api Error details")
@Getter
public abstract class ForbiddenProblemDetailSchema {
	@Schema(name = "type", example = "about:blank")
	private String type;
	@Schema(name = "title", example = "Forbidden")
	private String title;
	@Schema(name = "status", example = "403")
	private Integer status;
	@Schema(name = "detail", example = "Invalid startDate/finishDate format. Expected: dd/MM/yyyy HH:mm:ss")
	private String detail;
	@Schema(name = "instance", example = "/tasks/1")
	private String instance;
	@Schema(name = "description", example = "Request input is invalid")
	private String description;
}
