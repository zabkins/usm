package pl.zarczynski.usm.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import pl.zarczynski.usm.task.entity.TaskStatus;

@Data
@Schema(description = "DTO used for updating a task")
public class UpdateTaskDto {
	@Schema(description = "Task name", name = "name", requiredMode = Schema.RequiredMode.REQUIRED, example = "Task name")
	private String name;
	@Schema(description = "Task description", name = "description", requiredMode = Schema.RequiredMode.REQUIRED, example = "Task description")
	private String description;
	@Schema(description = "Task's start date in format [dd/MM/yyyy HH:mm:ss]", name = "startDate", requiredMode = Schema.RequiredMode.REQUIRED, example = "01/01/2025 08:00:00")
	private String startDate;
	@Schema(description = "Task's finish date in format [dd/MM/yyyy HH:mm:ss]", name = "finishDate", requiredMode = Schema.RequiredMode.REQUIRED, example = "15/01/2025 08:00:00")
	private String finishDate;
	@Schema(description = "Task's status", name = "status", requiredMode = Schema.RequiredMode.REQUIRED, example = "PLANNED", allowableValues = {"PLANNED", "IN_PROGRESS", "DONE", "CANCELLED"})
	private TaskStatus status;
}
