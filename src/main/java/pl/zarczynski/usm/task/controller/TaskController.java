package pl.zarczynski.usm.task.controller;

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
import pl.zarczynski.usm.swagger.auth.ForbiddenProblemDetailSchema;
import pl.zarczynski.usm.swagger.task.TaskNotFoundProblemDetailSchema;
import pl.zarczynski.usm.task.dto.CreateTaskDto;
import pl.zarczynski.usm.task.dto.TaskDto;
import pl.zarczynski.usm.task.dto.UpdateTaskDto;
import pl.zarczynski.usm.task.service.DtoValidator;
import pl.zarczynski.usm.task.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tasks", description = "Task related REST requests")
public class TaskController {

	private final TaskService taskService;
	private final DtoValidator dtoValidator;

	@GetMapping("/{id}")
	@Operation(description = "Get task with given ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TaskDto.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = TaskNotFoundProblemDetailSchema.class), mediaType = "application/json")),
	})
	public ResponseEntity<TaskDto> getTask(@PathVariable @Parameter(description = "Task ID", required = true) Long id){
		return ResponseEntity.ok(taskService.findTask(id));
	}

	@GetMapping()
	@Operation(description = "Get all tasks")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class)), mediaType = "application/json")}),
	})
	public ResponseEntity<List<TaskDto>> getAllTasks(){
		return ResponseEntity.ok(taskService.findTasks());
	}

	@PostMapping()
	@Operation(description = "Create task")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TaskDto.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ForbiddenProblemDetailSchema.class), mediaType = "application/json")),
	})
	public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskDto taskDto){
		dtoValidator.validate(taskDto);
		return ResponseEntity.ok(taskService.saveTask(taskDto));
	}

	@DeleteMapping("/{id}")
	@Operation(description = "Delete task")
	@ApiResponses({
			@ApiResponse(responseCode = "204", content = {@Content()}),
			@ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = TaskNotFoundProblemDetailSchema.class), mediaType = "application/json")),

	})
	public ResponseEntity<TaskDto> deleteTask(@PathVariable Long id){
		taskService.deleteTask(id);
		return ResponseEntity.status(204).build();
	}

	@PutMapping("/{id}")
	@Operation(description = "Update task")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TaskDto.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ForbiddenProblemDetailSchema.class), mediaType = "application/json")),
			@ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = TaskNotFoundProblemDetailSchema.class), mediaType = "application/json")),
	})
	public ResponseEntity<TaskDto> updateTask(@PathVariable @Parameter(description = "Task ID", required = true) Long id, @RequestBody UpdateTaskDto taskDto){
		dtoValidator.validate(taskDto);
		return ResponseEntity.ok(taskService.updateTask(id, taskDto));
	}
}
