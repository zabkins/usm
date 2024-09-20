package pl.zarczynski.usm.task.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.zarczynski.usm.configuration.user.User;
import pl.zarczynski.usm.task.dto.CreateTaskDto;
import pl.zarczynski.usm.task.dto.TaskDto;
import pl.zarczynski.usm.task.dto.UpdateTaskDto;
import pl.zarczynski.usm.task.entity.Task;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;

	public List<TaskDto> findTasks () {
		User currentUser = getCurrentUser();
		return taskRepository.findAllByUser(currentUser).stream()
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
		Task task = taskRepository.findByIdAndUser(id, getCurrentUser()).orElseThrow(() -> new EntityNotFoundException("Task with ID [" + id + "] not found"));
		return taskMapper.toDto(task);
	}

	private User getCurrentUser () {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (User) authentication.getPrincipal();
	}

	public void deleteTask (Long id) {
		Task task = taskRepository.findByIdAndUser(id, getCurrentUser()).orElseThrow(
				() -> new EntityNotFoundException("Task with ID [" + id + "] not found"));
		taskRepository.delete(task);
	}

	@Transactional
	public TaskDto updateTask (UpdateTaskDto dto) {
		Task taskToUpdate = taskRepository.findByIdAndUser(dto.getId(), getCurrentUser()).orElseThrow(
				() -> new EntityNotFoundException("Task with ID [" + dto.getId() + "] not found"));
		updateTask(taskToUpdate, dto);
		Task updatedTask = taskRepository.save(taskToUpdate);
		return taskMapper.toDto(updatedTask);
	}

	private void updateTask (Task taskToUpdate, UpdateTaskDto dto) {
		//TODO -> go through fields, check if different and update
	}
}
