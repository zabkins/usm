package pl.zarczynski.usm.task.dto;

import lombok.Data;
import pl.zarczynski.usm.task.entity.TaskStatus;
import pl.zarczynski.usm.subtask.dto.SubTaskDto;

import java.util.List;

@Data
public class TaskDto {
	private Long id;
	private String name;
	private String description;
	private String dateFrom;
	private String dateTo;
	private TaskStatus status;
	private List<SubTaskDto> subTasks;
}