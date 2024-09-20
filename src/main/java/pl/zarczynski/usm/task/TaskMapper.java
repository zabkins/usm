package pl.zarczynski.usm.task;

import org.springframework.stereotype.Service;
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.task.subtask.CreateSubTaskDto;
import pl.zarczynski.usm.task.subtask.SubTask;
import pl.zarczynski.usm.task.subtask.SubTaskDto;

import java.time.ZonedDateTime;

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
		task.setStartDate(DateHelper.parseStringToZonedDateTime(taskDto.getPlannedStartDate()));
		task.setFinishDate(DateHelper.parseStringToZonedDateTime(taskDto.getPlannedFinishDate()));
		task.setStatus(TaskStatus.PLANNED);
		if (taskDto.getSubTasks() != null && !taskDto.getSubTasks().isEmpty()) {
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
