package pl.zarczynski.usm.subtask.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zarczynski.usm.swagger.subtask.SubTaskNotFoundProblemDetailSchema;
import pl.zarczynski.usm.swagger.task.TaskNotFoundProblemDetailSchema;
import pl.zarczynski.usm.subtask.dto.CreateSubTaskDto;
import pl.zarczynski.usm.subtask.dto.SubTaskDto;
import pl.zarczynski.usm.subtask.dto.UpdateSubTaskDto;
import pl.zarczynski.usm.subtask.service.SubTaskService;
import pl.zarczynski.usm.task.service.DtoValidator;

import java.util.List;



@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SubTasks", description = "SubTask related REST requests")
public class SubTaskController {

	private final DtoValidator dtoValidator;
	private final SubTaskService subTaskService;

	@PostMapping("/{id}/subtasks")
	@Operation(description = "Create subtask for task with given ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SubTaskDto.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = TaskNotFoundProblemDetailSchema.class), mediaType = "application/json")),
	})
	public ResponseEntity<SubTaskDto> createSubTask(@PathVariable(value = "id") @Parameter(description = "Task ID", required = true) Long taskId, @RequestBody CreateSubTaskDto subTaskDto){
		dtoValidator.validate(subTaskDto);
		return ResponseEntity.ok(subTaskService.saveSubTask(taskId, subTaskDto));
	}

	@GetMapping("/subtasks/{id}")
	@Operation(description = "Get SubTask with given ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SubTaskDto.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = SubTaskNotFoundProblemDetailSchema.class), mediaType = "application/json"))
	})
	public ResponseEntity<SubTaskDto> findSubTask(@PathVariable(value = "id") @Parameter(description = "SubTask ID", required = true) Long subTaskId){
		return ResponseEntity.ok(subTaskService.findSubTask(subTaskId));
	}

	@GetMapping("/{id}/subtasks")
	@Operation(description = "Get all SubTasks for Task of given ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = SubTaskDto.class)), mediaType = "application/json")}),
	})
	public ResponseEntity<List<SubTaskDto>> findAllSubTasks(@PathVariable(value = "id") @Parameter(description = "Task ID", required = true) Long taskId){
		return ResponseEntity.ok(subTaskService.findSubTasksForGivenTask(taskId));
	}

	@DeleteMapping("/subtasks/{id}")
	@Operation(description = "Delete SubTask with given ID")
	@ApiResponses({
			@ApiResponse(responseCode = "204", content = {@Content()}),
			@ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = SubTaskNotFoundProblemDetailSchema.class), mediaType = "application/json"))
	})
	public ResponseEntity<SubTaskDto> deleteSubTask(@PathVariable(value = "id") @Parameter(description = "SubTask ID", required = true) Long subTaskId){
		subTaskService.deleteSubTask(subTaskId);
		return ResponseEntity.status(204).build();
	}

	@PutMapping("/subtasks/{id}")
	@Operation(description = "Update SubTask")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = SubTaskDto.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = SubTaskNotFoundProblemDetailSchema.class), mediaType = "application/json")),
	})
	public ResponseEntity<SubTaskDto> updateSubTask(@PathVariable(value = "id") @Parameter(description = "SubTask ID", required = true) Long subTaskId, @RequestBody UpdateSubTaskDto updateSubTaskDto){
		return ResponseEntity.ok(subTaskService.updateSubTask(subTaskId, updateSubTaskDto));
	}
}
