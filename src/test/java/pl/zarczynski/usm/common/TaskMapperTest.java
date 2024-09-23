package pl.zarczynski.usm.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zarczynski.usm.subtask.dto.CreateSubTaskDto;
import pl.zarczynski.usm.subtask.dto.SubTaskDto;
import pl.zarczynski.usm.subtask.entity.SubTask;
import pl.zarczynski.usm.task.dto.CreateTaskDto;
import pl.zarczynski.usm.task.dto.TaskDto;
import pl.zarczynski.usm.task.entity.Task;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {

	private final TaskMapper taskMapper = new TaskMapper();
	@Test
	void shouldMapToTask() {
		//given
		CreateTaskDto dto = new CreateTaskDto();
		dto.setName("test name");
		dto.setDescription("test description");
		String start = DateHelper.parseDate(ZonedDateTime.now());
		String finish = DateHelper.parseDate(ZonedDateTime.now().plusWeeks(1));
		dto.setStartDate(start.substring(0, start.lastIndexOf(" ")));
		dto.setFinishDate(finish.substring(0, finish.lastIndexOf(" ")));
		//when
		Task task = taskMapper.fromDto(dto);
		//then
		assertEquals(dto.getName(), task.getName());
		assertEquals(dto.getDescription(), task.getDescription());
		assertEquals(DateHelper.parseStringToZonedDateTime(dto.getStartDate()), task.getStartDate());
		assertEquals(DateHelper.parseStringToZonedDateTime(dto.getFinishDate()), task.getFinishDate());
	}

	@Test
	void shouldMapToSubTask() {
		//given
		CreateSubTaskDto dto = new CreateSubTaskDto();
		dto.setName("test name");
		dto.setDescription("test description");
		//when
		SubTask subTask = taskMapper.fromDto(dto);
		//then
		assertEquals(dto.getName(), subTask.getName());
		assertEquals(dto.getDescription(), subTask.getDescription());
	}

	@Test
	void shouldMapToTaskDto() {
		//given
		Task task = new Task();
		task.setName("test name");
		task.setDescription("test description");
		ZonedDateTime now = ZonedDateTime.now();
		task.setStartDate(now);
		task.setFinishDate(now.plusWeeks(1));
		//when
		TaskDto dto = taskMapper.toDto(task);
		//then
		assertEquals(task.getName(), dto.getName());
		assertEquals(task.getDescription(), dto.getDescription());
	}

	@Test
	void shouldMapToSubTaskDto() {
		//given
		SubTask subTask = new SubTask();
		subTask.setName("test name");
		subTask.setDescription("test description");
		subTask.setDone(true);
		//when
		SubTaskDto dto = taskMapper.toDto(subTask);
		//then
		assertEquals(subTask.getName(), dto.getName());
		assertEquals(subTask.getDescription(), dto.getDescription());
		assertEquals(subTask.isDone(), dto.isDone());
	}
}