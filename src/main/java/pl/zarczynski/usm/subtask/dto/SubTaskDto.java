package pl.zarczynski.usm.subtask.dto;

import lombok.Data;

@Data
public class SubTaskDto {

	private Long id;
	private String name;
	private String description;
	private boolean isDone;
}
