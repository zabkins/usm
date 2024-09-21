package pl.zarczynski.usm.subtask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO for creating a SubTask")
public class CreateSubTaskDto {
	@Schema(description = "SubTask name", name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
	private String name;
	@Schema(description = "SubTask description", name = "description", requiredMode = Schema.RequiredMode.REQUIRED)
	private String description;
}
