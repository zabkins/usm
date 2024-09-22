package pl.zarczynski.usm.swaggerschemas.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Page details")
@Getter
public abstract class PageSchema {
	@Schema(name = "size", description = "Page size", example = "5")
	private Integer size;
	@Schema(name = "number", description = "Page number", example = "0")
	private Integer number;
	@Schema(name = "totalElements", description = "Total number of elements found", example = "1")
	private Integer totalElements;
	@Schema(name = "totalPage", description = "Total pages found", example = "1")
	private Integer totalPages;
}
