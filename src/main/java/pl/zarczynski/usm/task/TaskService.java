package pl.zarczynski.usm.task;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.zarczynski.usm.configuration.user.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;

	public List<TaskDto> findTasks() {
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

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return  (User) authentication.getPrincipal();
	}
}
