package pl.zarczynski.usm.subtask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO representing SubTask")
public class SubTaskDto {
	@Schema(description = "SubTask's ID", name = "id")
	private Long id;
	@Schema(description = "SubTask's name", name = "name")
	private String name;
	@Schema(description = "SubTask's description", name = "description")
	private String description;
	@Schema(description = "SubTask's done status", name = "isDone")
	private boolean isDone;
}
