package pl.zarczynski.usm.task.dto;

import lombok.Data;
import pl.zarczynski.usm.task.subtask.CreateSubTaskDto;

import java.util.List;

@Data
public class CreateTaskDto {
	private String name;
	private String description;
	private String startDate;
	private String finishDate;
	private List<CreateSubTaskDto> subTasks;
}
