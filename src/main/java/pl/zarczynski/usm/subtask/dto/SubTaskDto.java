package pl.zarczynski.usm.subtask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO representing SubTask")
public class SubTaskDto {
	@Schema(description = "SubTask's ID", name = "id", example = "1")
	private Long id;
	@Schema(description = "SubTask's name", name = "name", example = "SubTask name")
	private String name;
	@Schema(description = "SubTask's description", name = "description", example = "SubTask description")
	private String description;
	@Schema(description = "SubTask's done status", name = "isDone", example = "true/false")
	private boolean isDone;
}
