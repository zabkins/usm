package pl.zarczynski.usm.subtask.dto;

import lombok.Data;

@Data
public class UpdateSubTaskDto {
	private String name;
	private String description;
	private boolean done;
}
