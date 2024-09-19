package pl.zarczynski.usm.task;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.zarczynski.usm.configuration.user.User;

import java.util.List;

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

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return  (User) authentication.getPrincipal();
	}
}
