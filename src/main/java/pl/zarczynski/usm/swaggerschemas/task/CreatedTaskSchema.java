package pl.zarczynski.usm.swaggerschemas.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import pl.zarczynski.usm.task.entity.TaskStatus;

@Schema(description = "DTO used for representing a task")
@Getter
public abstract class CreatedTaskSchema {
	@Schema(description = "ID of the task", name = "id", example = "1")
	private Long id;
	@Schema(description = "Name of the task", name = "name", example = "Task name")
	private String name;
	@Schema(description = "Description of the task", name = "description", example = "Task description")
	private String description;
	@Schema(description = "Date when task starts", name = "startDate", example = "21/01/2024 08:00:00 CEST")
	private String startDate;
	@Schema(description = "Date when task ends", name = "finishDate", example = "25/01/2024 16:00:00 CEST")
	private String finishDate;
	@Schema(description = "Status of the task", name = "status", example = "PLANNED")
	private TaskStatus status;
	@Schema(description = "SubTasks of the task", name = "subTasks", example = "[]")
	private String subTasks;
}