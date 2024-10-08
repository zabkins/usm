package pl.zarczynski.usm.swaggerschemas.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Api Error details")
@Getter
public abstract class TaskNotFoundProblemDetailSchema {
	@Schema(name = "type", example = "about:blank")
	private String type;
	@Schema(name = "title", example = "Not Found")
	private String title;
	@Schema(name = "status", example = "404")
	private Integer status;
	@Schema(name = "detail", example = "Task with ID [1] not found")
	private String detail;
	@Schema(name = "instance", example = "/tasks/1")
	private String instance;
	@Schema(name = "description", example = "Resource not found")
	private String description;
}
