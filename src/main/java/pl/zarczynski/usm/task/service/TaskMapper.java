package pl.zarczynski.usm.task.service;

import org.springframework.stereotype.Service;
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.task.dto.TaskDto;
import pl.zarczynski.usm.task.entity.TaskStatus;
import pl.zarczynski.usm.task.dto.CreateTaskDto;
import pl.zarczynski.usm.task.entity.Task;
import pl.zarczynski.usm.task.subtask.CreateSubTaskDto;
import pl.zarczynski.usm.task.subtask.SubTask;
import pl.zarczynski.usm.task.subtask.SubTaskDto;

import java.util.Collections;

@Service
public class TaskMapper {

	TaskDto toDto (Task task) {
		TaskDto dto = new TaskDto();
		dto.setId(task.getId());
		dto.setName(task.getName());
		dto.setDescription(task.getDescription());
		dto.setDateFrom(DateHelper.parseDate(task.getStartDate()));
		dto.setDateTo(DateHelper.parseDate(task.getFinishDate()));
		dto.setStatus(task.getStatus());
		if (task.getSubTasks() != null && !task.getSubTasks().isEmpty()) {
			dto.setSubTasks(task.getSubTasks().stream()
					.map(this::toDto)
					.toList());
		} else {
			dto.setSubTasks(Collections.emptyList());
		}
		return dto;
	}

	SubTaskDto toDto (SubTask subTask) {
		SubTaskDto dto = new SubTaskDto();
		dto.setId(subTask.getId());
		dto.setName(subTask.getName());
		dto.setDescription(subTask.getDescription());
		dto.setDone(subTask.isDone());
		return dto;
	}

	Task fromDto (CreateTaskDto taskDto) {
		Task task = new Task();
		task.setName(taskDto.getName());
		task.setDescription(taskDto.getDescription());
		task.setStartDate(DateHelper.parseStringToZonedDateTime(taskDto.getStartDate()));
		task.setFinishDate(DateHelper.parseStringToZonedDateTime(taskDto.getFinishDate()));
		task.setStatus(TaskStatus.PLANNED);
		return task;
	}

	SubTask fromDto (CreateSubTaskDto subTaskDto) {
		SubTask subTask = new SubTask();
		subTask.setName(subTaskDto.getName());
		subTask.setDescription(subTaskDto.getDescription());
		subTask.setDone(subTaskDto.isDone());
		return subTask;
	}
}
