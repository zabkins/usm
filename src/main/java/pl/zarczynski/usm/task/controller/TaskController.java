package pl.zarczynski.usm.task.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zarczynski.usm.task.dto.UpdateTaskDto;
import pl.zarczynski.usm.task.service.DtoValidator;
import pl.zarczynski.usm.task.dto.CreateTaskDto;
import pl.zarczynski.usm.task.dto.TaskDto;
import pl.zarczynski.usm.task.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tasks")
public class TaskController {

	private final TaskService taskService;
	private final DtoValidator dtoValidator;

	@GetMapping("/{id}")
	public ResponseEntity<TaskDto> getTask(@PathVariable Long id){
		return ResponseEntity.ok(taskService.findTask(id));
	}

	@GetMapping()
	public ResponseEntity<List<TaskDto>> getAllTasks(){
		return ResponseEntity.ok(taskService.findTasks());
	}

	@PostMapping()
	public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskDto taskDto){
		dtoValidator.validate(taskDto);
		return ResponseEntity.ok(taskService.saveTask(taskDto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<TaskDto> deleteTask(@PathVariable Long id){
		taskService.deleteTask(id);
		return ResponseEntity.status(204).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<TaskDto> updateTask(@RequestBody UpdateTaskDto taskDto){
		dtoValidator.validate(taskDto);
		return ResponseEntity.ok(taskService.updateTask(taskDto));
	}
}
