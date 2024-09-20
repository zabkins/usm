package pl.zarczynski.usm.task;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
