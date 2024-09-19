package pl.zarczynski.usm.task;

import lombok.Data;
import pl.zarczynski.usm.task.subtask.CreateSubTaskDto;
import pl.zarczynski.usm.task.subtask.SubTaskDto;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class CreateTaskDto {
	private String name;
	private String description;
	//TODO -> parsowanie tych dat w request z JSON (chyba na String)
	private ZonedDateTime dateFrom;
	private ZonedDateTime dateTo;
	private TaskStatus status;
	private List<CreateSubTaskDto> subTasks;
}
