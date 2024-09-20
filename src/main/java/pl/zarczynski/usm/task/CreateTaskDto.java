package pl.zarczynski.usm.task;

import lombok.Data;
import pl.zarczynski.usm.task.subtask.CreateSubTaskDto;

import java.util.List;

@Data
public class CreateTaskDto {
	private String name;
	private String description;
	private String plannedStartDate;
	private String plannedFinishDate;
	private List<CreateSubTaskDto> subTasks;
}
