package pl.zarczynski.usm.swaggerschemas.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import pl.zarczynski.usm.task.dto.TaskDto;

import java.util.List;

@Schema(description = "Page with list of fetched tasks")
@Getter
public abstract class CreatedTaskPageContentSchema {
	@Schema(description = "Fetched tasks", name = "content")
	private List<TaskDto> content;
	@Schema(description = "Page details", name = "page")
	private PageSchema page;
}