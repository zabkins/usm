package pl.zarczynski.usm.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO used for creating a task")
public class CreateTaskDto {
	@Schema(description = "Task name", name = "name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Task name")
	private String name;
	@Schema(description = "Task description", name = "description", requiredMode = Schema.RequiredMode.REQUIRED, example = "Task description")
	private String description;
	@Schema(description = "Task's start date in format [dd/MM/yyyy HH:mm:ss]", name = "startDate", requiredMode = Schema.RequiredMode.REQUIRED, example = "dd/MM/yyyy HH:mm:ss")
	private String startDate;
	@Schema(description = "Task's finish date in format [dd/MM/yyyy HH:mm:ss]", name = "finishDate", requiredMode = Schema.RequiredMode.REQUIRED, example = "dd/MM/yyyy HH:mm:ss")
	private String finishDate;
}
