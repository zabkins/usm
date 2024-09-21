package pl.zarczynski.usm.subtask.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@Tag(name = "SubTasks")
public class SubTaskController {

	private final DtoValidator dtoValidator;
	private final SubTaskService subTaskService;

	@PostMapping("/{id}/subtasks")
	public ResponseEntity<SubTaskDto> createSubTask(@PathVariable(value = "id") Long taskId, @RequestBody CreateSubTaskDto subTaskDto){
		dtoValidator.validate(subTaskDto);
		return ResponseEntity.ok(subTaskService.saveSubTask(taskId, subTaskDto));
	}

	@GetMapping("/subtasks/{id}")
	public ResponseEntity<SubTaskDto> findSubTask(@PathVariable(value = "id") Long subTaskId){
		return ResponseEntity.ok(subTaskService.findSubTask(subTaskId));
	}

	@GetMapping("/{id}/subtasks")
	public ResponseEntity<List<SubTaskDto>> findAllSubTasks(@PathVariable(value = "id") Long taskId){
		return ResponseEntity.ok(subTaskService.findSubTasksForGivenTask(taskId));
	}
	@DeleteMapping("/subtasks/{id}")
	public ResponseEntity<SubTaskDto> deleteSubTask(@PathVariable(value = "id") Long subTaskId){
		subTaskService.deleteSubTask(subTaskId);
		return ResponseEntity.status(204).build();
	}

	@PutMapping("/subtasks/{id}")
	public ResponseEntity<SubTaskDto> updateSubTask(@PathVariable(value = "id") Long subTaskId, @RequestBody UpdateSubTaskDto updateSubTaskDto){
		return ResponseEntity.ok(subTaskService.updateSubTask(subTaskId, updateSubTaskDto));
	}
}
