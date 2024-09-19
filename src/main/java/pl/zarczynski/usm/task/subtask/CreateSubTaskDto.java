package pl.zarczynski.usm.task.subtask;

import lombok.Data;

@Data
public class CreateSubTaskDto {
	private String name;
	private String description;
	private boolean isDone;
}
