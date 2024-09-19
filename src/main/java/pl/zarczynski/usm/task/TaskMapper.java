package pl.zarczynski.usm.task;

import org.springframework.stereotype.Service;
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.task.subtask.SubTask;
import pl.zarczynski.usm.task.subtask.SubTaskDto;

@Service
public class TaskMapper {

	TaskDto toDto(Task task) {
		TaskDto dto = new TaskDto();
		dto.setId(task.getId());
		dto.setName(task.getName());
		dto.setDescription(task.getDescription());
		dto.setDateFrom(DateHelper.parseDate(task.getDateFrom()));
		dto.setDateTo(DateHelper.parseDate(task.getDateTo()));
		dto.setStatus(task.getStatus().name());
		dto.setSubTasks(task.getSubTasks().stream()
				.map(this::toDto)
				.toList());
		return dto;
	}

	SubTaskDto toDto(SubTask subTask) {
		SubTaskDto dto = new SubTaskDto();
		dto.setId(subTask.getId());
		dto.setName(subTask.getName());
		dto.setDone(subTask.isDone());
		return dto;
	}
}
