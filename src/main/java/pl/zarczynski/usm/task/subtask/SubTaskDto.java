package pl.zarczynski.usm.task.subtask;

import lombok.Data;

@Data
public class SubTaskDto {

	private Long id;
	private String name;
	private boolean isDone;
}
