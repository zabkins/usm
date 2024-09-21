package pl.zarczynski.usm.subtask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO for updating a SubTask")
public class UpdateSubTaskDto {
	@Schema(description = "SubTask name", name = "name", requiredMode = Schema.RequiredMode.REQUIRED, example = "SubTask name")
	private String name;
	@Schema(description = "SubTask description", name = "description", requiredMode = Schema.RequiredMode.REQUIRED, example = "SubTask description")
	private String description;
	@Schema(description = "SubTask's done status", name = "done", requiredMode = Schema.RequiredMode.REQUIRED, example = "true/false")
	private boolean done;
}
