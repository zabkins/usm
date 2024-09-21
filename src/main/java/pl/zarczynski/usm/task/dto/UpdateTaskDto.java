package pl.zarczynski.usm.task.dto;

import lombok.Data;
import pl.zarczynski.usm.task.entity.TaskStatus;
import pl.zarczynski.usm.task.subtask.UpdateSubTaskDto;

import java.util.List;

@Data
public class UpdateTaskDto {
	private String name;
	private String description;
	private String startDate;
	private String finishDate;
	private TaskStatus status;
}
