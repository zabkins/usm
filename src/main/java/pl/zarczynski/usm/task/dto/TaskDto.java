package pl.zarczynski.usm.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import pl.zarczynski.usm.task.entity.TaskStatus;
import pl.zarczynski.usm.subtask.dto.SubTaskDto;

import java.util.List;

@Data
@Schema(description = "DTO used for representing a task")
public class TaskDto {
	@Schema(description = "ID of the task", name = "id")
	private Long id;
	@Schema(description = "Name of the task", name = "name")
	private String name;
	@Schema(description = "Description of the task", name = "description")
	private String description;
	@Schema(description = "Date when task starts", name = "startDate")
	private String startDate;
	@Schema(description = "Date when task ends", name = "finishDate")
	private String finishDate;
	@Schema(description = "Status of the task", name = "status")
	private TaskStatus status;
	@Schema(description = "SubTasks of the task", name = "subTasks")
	private List<SubTaskDto> subTasks;
}