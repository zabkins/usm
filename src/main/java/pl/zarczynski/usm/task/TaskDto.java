package pl.zarczynski.usm.task;

import lombok.Data;
import pl.zarczynski.usm.task.subtask.SubTaskDto;

import java.util.List;

@Data
public class TaskDto {
	private Long id;
	private String name;
	private String description;
	private String dateFrom;
	private String dateTo;
	private String status;
	private List<SubTaskDto> subTasks;
}