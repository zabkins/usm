package pl.zarczynski.usm.task.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.common.TaskMapper;
import pl.zarczynski.usm.configuration.user.User;
import pl.zarczynski.usm.task.dto.CreateTaskDto;
import pl.zarczynski.usm.task.dto.TaskDto;
import pl.zarczynski.usm.task.dto.UpdateTaskDto;
import pl.zarczynski.usm.task.entity.Task;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;

	public List<TaskDto> findTasks () {
		User currentUser = getCurrentUser();
		List<Task> foundTasks = taskRepository.findAllByUser(currentUser);
		return foundTasks.isEmpty() ? Collections.emptyList() : foundTasks.stream()
				.map(taskMapper::toDto)
				.toList();
	}

	public TaskDto saveTask (CreateTaskDto taskDto) {
		Task task = taskMapper.fromDto(taskDto);
		task.setUser(getCurrentUser());
		Task savedTask = taskRepository.save(task);
		return taskMapper.toDto(savedTask);
	}

	public TaskDto findTask (Long id) {
		Task task = taskRepository.findByIdAndUserWithSubtasks(id, getCurrentUser()).orElseThrow(() -> new EntityNotFoundException("Task with ID [" + id + "] not found"));
		return taskMapper.toDto(task);
	}

	private User getCurrentUser () {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (User) authentication.getPrincipal();
	}

	public void deleteTask (Long id) {
		Task task = taskRepository.findByIdAndUserWithSubtasks(id, getCurrentUser()).orElseThrow(
				() -> new EntityNotFoundException("Task with ID [" + id + "] not found"));
		taskRepository.delete(task);
	}

	public TaskDto updateTask (Long id, UpdateTaskDto dto) {
		Task taskToUpdate = taskRepository.findByIdAndUser(id, getCurrentUser()).orElseThrow(
				() -> new EntityNotFoundException("Task with ID [" + id + "] not found"));
		updateTask(taskToUpdate, dto);
		Task updatedTask = taskRepository.save(taskToUpdate);
		return taskMapper.toDto(updatedTask);
	}

	private void updateTask (Task taskToUpdate, UpdateTaskDto dto) {
		if (!taskToUpdate.getName().equals(dto.getName())) {
			taskToUpdate.setName(dto.getName());
		}
		if (!taskToUpdate.getDescription().equals(dto.getDescription())) {
			taskToUpdate.setDescription(dto.getDescription());
		}
		ZonedDateTime dtoStartDate = DateHelper.parseStringToZonedDateTime(dto.getStartDate());
		if (!taskToUpdate.getStartDate().equals(dtoStartDate)) {
			taskToUpdate.setStartDate(dtoStartDate);
		}
		ZonedDateTime dtoFinishDate = DateHelper.parseStringToZonedDateTime(dto.getFinishDate());
		if (!taskToUpdate.getFinishDate().equals(dtoFinishDate)) {
			taskToUpdate.setFinishDate(dtoFinishDate);
		}
		if (!taskToUpdate.getStatus().equals(dto.getStatus())) {
			taskToUpdate.setStatus(dto.getStatus());
		}
	}

}
