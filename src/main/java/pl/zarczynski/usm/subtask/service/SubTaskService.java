package pl.zarczynski.usm.subtask.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import pl.zarczynski.usm.task.service.TaskRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubTaskService {

	private final SubTaskRepository subTaskRepository;
	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;

	@Transactional
	public SubTaskDto saveSubTask (Long taskId, CreateSubTaskDto subTaskDto) {
		Task existingTask = taskRepository.findByIdAndUserWithSubtasks(taskId, getCurrentUser()).orElseThrow(
				() -> new EntityNotFoundException("Task with ID [" + taskId + "] not found"));
		SubTask subTask = taskMapper.fromDto(subTaskDto);
		subTask.setTask(existingTask);
		SubTask savedSubTask = subTaskRepository.save(subTask);
		existingTask.addSubTask(savedSubTask);
		taskRepository.save(existingTask);
		return taskMapper.toDto(savedSubTask);
	}

	public SubTaskDto findSubTask (Long subTaskId) {
		SubTask subTask = subTaskRepository.findSubTaskByIdAndUser(subTaskId, getCurrentUser()).orElseThrow(() -> new EntityNotFoundException("SubTask with ID [" + subTaskId + "] not found"));
		return taskMapper.toDto(subTask);
	}

	public void deleteSubTask (Long subTaskId) {
		SubTask subTask = subTaskRepository.findSubTaskByIdAndUser(subTaskId, getCurrentUser()).orElseThrow(() -> new EntityNotFoundException("SubTask with ID [" + subTaskId + "] not found"));
		subTaskRepository.delete(subTask);
	}

	public SubTaskDto updateSubTask (Long subTaskId, UpdateSubTaskDto updateSubTaskDto) {
		SubTask subTask = subTaskRepository.findSubTaskByIdAndUser(subTaskId, getCurrentUser()).orElseThrow(() -> new EntityNotFoundException("SubTask with ID [" + subTaskId + "] not found"));
		updateSubTask(subTask, updateSubTaskDto);
		SubTask updatedSubTask = subTaskRepository.save(subTask);
		return taskMapper.toDto(updatedSubTask);
	}

	public List<SubTaskDto> findSubTasksForGivenTask (Long taskId) {
		List<SubTask> subTasks = subTaskRepository.findAllSubTasksForGivenTask(taskId, getCurrentUser());
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
