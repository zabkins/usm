package pl.zarczynski.usm.task;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;
	@Value("${max.page.size:10}")
	private Integer maxPageSize;
	@Value("${default.sort-by:id}")
	private String defaultSortBy;

	public Page<TaskDto> findTasks (Optional<Integer> page, Optional<Integer> size, Optional<String> sortBy) {
		User currentUser = getCurrentUser();
		Integer pageNumber = page.orElse(0);
		Integer pageSize = size.orElse(maxPageSize);
		String toSortBy = sortBy.orElse(defaultSortBy);
		log.info("Finding tasks for user {}. Fetch parameters: [page={},size={},sortBy={}]", currentUser.getEmail(), pageNumber, pageSize, toSortBy);
		Page<Task> allTasksByUser = taskRepository.findAllByUser(currentUser, PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, toSortBy));
		log.info("Found {} tasks. {}", allTasksByUser.getTotalElements(), allTasksByUser.getContent());
		return allTasksByUser.map(taskMapper::toDto);
	}

	public TaskDto saveTask (CreateTaskDto taskDto) {
		log.info("Creating new Task {}", taskDto);
		Task task = taskMapper.fromDto(taskDto);
		task.setUser(getCurrentUser());
		Task savedTask = taskRepository.save(task);
		log.info("Task created: {}", savedTask);
		return taskMapper.toDto(savedTask);
	}

	public TaskDto findTask (Long id) {
		log.info("Finding task with id {}", id);
		Task task = taskRepository.findByIdAndUserWithSubtasks(id, getCurrentUser()).orElseThrow(() -> new EntityNotFoundException("Task with ID [" + id + "] not found"));
		log.info("Task found: {}", task);
		return taskMapper.toDto(task);
	}

	private User getCurrentUser () {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (User) authentication.getPrincipal();
	}

	public void deleteTask (Long id) {
		log.info("Deleting task with id {}", id);
		Task task = taskRepository.findByIdAndUserWithSubtasks(id, getCurrentUser()).orElseThrow(
				() -> new EntityNotFoundException("Task with ID [" + id + "] not found"));
		taskRepository.delete(task);
		log.info("Task with id {} deleted", id);
	}

	public TaskDto updateTask (Long id, UpdateTaskDto dto) {
		log.info("Updating task with id {}. Request data: {}", id, dto);
		Task taskToUpdate = taskRepository.findByIdAndUser(id, getCurrentUser()).orElseThrow(
				() -> new EntityNotFoundException("Task with ID [" + id + "] not found"));
		log.info("Task before update: {}", taskToUpdate);
		updateTask(taskToUpdate, dto);
		Task updatedTask = taskRepository.save(taskToUpdate);
		log.info("Task after update: {}", updatedTask);
		TaskDto taskDto = taskMapper.toDto(updatedTask);
		log.info("Returning {}", taskDto);
		return taskDto;
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
