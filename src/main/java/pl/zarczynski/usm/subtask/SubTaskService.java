package pl.zarczynski.usm.subtask;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.zarczynski.usm.configuration.user.User;
import pl.zarczynski.usm.subtask.dto.CreateSubTaskDto;
import pl.zarczynski.usm.subtask.dto.SubTaskDto;
import pl.zarczynski.usm.subtask.dto.UpdateSubTaskDto;
import pl.zarczynski.usm.subtask.entity.SubTask;
import pl.zarczynski.usm.task.entity.Task;
import pl.zarczynski.usm.common.TaskMapper;
import pl.zarczynski.usm.task.TaskRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubTaskService {

	private final SubTaskRepository subTaskRepository;
	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;

	@Transactional
	public SubTaskDto saveSubTask (Long taskId, CreateSubTaskDto subTaskDto) {
		log.info("Creating new SubTask for Task with ID {}. Request data: {}", taskId, subTaskDto);
		Task existingTask = taskRepository.findByIdAndUserWithSubtasks(taskId, getCurrentUser()).orElseThrow(
				() -> new EntityNotFoundException("Task with ID [" + taskId + "] not found"));
		SubTask subTask = taskMapper.fromDto(subTaskDto);
		subTask.setTask(existingTask);
		SubTask savedSubTask = subTaskRepository.save(subTask);
		existingTask.addSubTask(savedSubTask);
		taskRepository.save(existingTask);
		log.info("SubTask created: {}", savedSubTask);
		return taskMapper.toDto(savedSubTask);
	}

	public SubTaskDto findSubTask (Long subTaskId) {
		log.info("Finding SubTask with ID {}", subTaskId);
		SubTask subTask = subTaskRepository.findSubTaskByIdAndUser(subTaskId, getCurrentUser()).orElseThrow(() -> new EntityNotFoundException("SubTask with ID [" + subTaskId + "] not found"));
		log.info("SubTask found {}", subTask);
		return taskMapper.toDto(subTask);
	}

	public void deleteSubTask (Long subTaskId) {
		log.info("Deleting SubTask with ID {}", subTaskId);
		SubTask subTask = subTaskRepository.findSubTaskByIdAndUser(subTaskId, getCurrentUser()).orElseThrow(() -> new EntityNotFoundException("SubTask with ID [" + subTaskId + "] not found"));
		subTaskRepository.delete(subTask);
		log.info("SubTask deleted: {}", subTask);
	}

	public SubTaskDto updateSubTask (Long subTaskId, UpdateSubTaskDto updateSubTaskDto) {
		log.info("Updating SubTask with ID {}", subTaskId);
		SubTask subTask = subTaskRepository.findSubTaskByIdAndUser(subTaskId, getCurrentUser()).orElseThrow(() -> new EntityNotFoundException("SubTask with ID [" + subTaskId + "] not found"));
		log.info("SubTask before update: {}", subTask);
		updateSubTask(subTask, updateSubTaskDto);
		SubTask updatedSubTask = subTaskRepository.save(subTask);
		log.info("SubTask after update: {}", updatedSubTask);
		return taskMapper.toDto(updatedSubTask);
	}

	public List<SubTaskDto> findSubTasksForGivenTask (Long taskId) {
		log.info("Finding SubTasks for Task with ID {}", taskId);
		List<SubTask> subTasks = subTaskRepository.findAllSubTasksForGivenTask(taskId, getCurrentUser());
		log.info("SubTasks found: {}", subTasks);
		return subTasks.isEmpty() ? Collections.emptyList() : subTasks.stream()
				.map(taskMapper::toDto)
				.toList();
	}

	private void updateSubTask (SubTask subTask, UpdateSubTaskDto subTaskDto) {
		if (!subTask.getName().equals(subTaskDto.getName())) {
			subTask.setName(subTaskDto.getName());
		}
		if (!subTask.getDescription().equals(subTaskDto.getDescription())) {
			subTask.setDescription(subTaskDto.getDescription());
		}
		if (subTask.isDone() != subTaskDto.isDone()) {
			subTask.setDone(subTaskDto.isDone());
		}
	}

	private User getCurrentUser () {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (User) authentication.getPrincipal();
	}
}
