package pl.zarczynski.usm.task.dto;

import lombok.Data;

@Data
public class UpdateTaskDto {
	private Long id;
	private String name;
	private String description;
	private String startDate;
	private String finishDate;
}
