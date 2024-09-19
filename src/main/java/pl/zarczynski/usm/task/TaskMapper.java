package pl.zarczynski.usm.task;

import org.springframework.stereotype.Service;
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.task.subtask.CreateSubTaskDto;
import pl.zarczynski.usm.task.subtask.SubTask;
import pl.zarczynski.usm.task.subtask.SubTaskDto;

@Service
public class TaskMapper {

	TaskDto toDto (Task task) {
		TaskDto dto = new TaskDto();
		dto.setId(task.getId());
		dto.setName(task.getName());
		dto.setDescription(task.getDescription());
		dto.setDateFrom(task.getDateFrom() != null ? DateHelper.parseDate(task.getDateFrom()) : null);
		dto.setDateTo(task.getDateTo() != null ? DateHelper.parseDate(task.getDateTo()) : null);
		dto.setStatus(task.getStatus());
		if (task.getSubTasks() != null) {
			dto.setSubTasks(task.getSubTasks().stream()
					.map(this::toDto)
					.toList());
		}
		return dto;
	}

	SubTaskDto toDto (SubTask subTask) {
		SubTaskDto dto = new SubTaskDto();
		dto.setId(subTask.getId());
		dto.setName(subTask.getName());
		dto.setDone(subTask.isDone());
		return dto;
	}

	Task fromDto (CreateTaskDto taskDto) {
		Task task = new Task();
		task.setName(taskDto.getName());
		task.setDescription(taskDto.getDescription());
		task.setDateFrom(taskDto.getDateFrom());
		task.setDateTo(taskDto.getDateTo());
		task.setStatus(taskDto.getStatus());
		if (taskDto.getSubTasks() != null) {
			task.setSubTasks(taskDto.getSubTasks().stream()
					.map(subTaskDto -> {
						SubTask subTask = fromDto(subTaskDto);
						subTask.setTask(task);
						return subTask;
					})
					.toList());
		}
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
