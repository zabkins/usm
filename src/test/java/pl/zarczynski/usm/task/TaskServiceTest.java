package pl.zarczynski.usm.task;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.common.TaskMapper;
import pl.zarczynski.usm.security.WithMockCustomUser;
import pl.zarczynski.usm.task.dto.CreateTaskDto;
import pl.zarczynski.usm.task.dto.TaskDto;
import pl.zarczynski.usm.task.dto.UpdateTaskDto;
import pl.zarczynski.usm.task.entity.Task;
import pl.zarczynski.usm.task.entity.TaskStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WithMockCustomUser
class TaskServiceTest {

	@InjectMocks
	private TaskService service;
	@Mock
	private TaskRepository repository;
	@Mock
	private TaskMapper mapper;


	@Test
	void shouldSaveTask() {
		//given
		when(mapper.fromDto(any(CreateTaskDto.class))).thenReturn(createTask());
		when(repository.save(any())).thenReturn(createTask());
		//when
		TaskDto taskDto = service.saveTask(new CreateTaskDto());
		//then
		verify(mapper).fromDto(any(CreateTaskDto.class));
		verify(repository).save(any(Task.class));
		verify(mapper).toDto(any(Task.class));
	}

	@Test
	void shouldNotThrowExceptionWhenTaskFound() {
		//given
		when(repository.findByIdAndUserWithSubtasks(any(), any())).thenReturn(Optional.of(createTask()));
		//when
		assertDoesNotThrow(() -> service.findTask(1L));
		//
		verify(mapper).toDto(any(Task.class));
	}

	@Test
	void shouldThrowExceptionWhenTaskNotFound() {
		//given
		when(repository.findByIdAndUserWithSubtasks(any(), any())).thenReturn(Optional.empty());
		//when
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.findTask(1L));
		//then
		assertEquals("Task with ID [1] not found", exception.getMessage());
		verify(mapper, never()).toDto(any(Task.class));
	}

	@Test
	void shouldThrowAnExceptionWhenTryingToDeleteNonExistingTask() {
		//given
		when(repository.findByIdAndUserWithSubtasks(any(), any())).thenReturn(Optional.empty());
		//when
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.deleteTask(1L));
		//then
		assertEquals("Task with ID [1] not found", exception.getMessage());
		verify(repository, never()).delete(any());
	}

	@Test
	void shouldNotThrowAnExceptionWhenTryingToDeleteExistingTask() {
		//given
		when(repository.findByIdAndUserWithSubtasks(any(), any())).thenReturn(Optional.of(createTask()));
		//when
		assertDoesNotThrow(() -> service.deleteTask(1L));
		//then
		verify(repository).delete(any());
	}

	@Test
	void shouldUpdateTask() {
		//given
		when(repository.findByIdAndUser(any(), any())).thenReturn(Optional.of(createTask()));
		Task task = createTask();
		TaskDto taskDto = new TaskDto();
		taskDto.setName("Updated name");
		taskDto.setDescription("Updated description");
		when(repository.save(any())).thenReturn(task);
		when(mapper.toDto(task)).thenReturn(taskDto);
		//when
		TaskDto updatedTask = service.updateTask(1L, createUpdateTaskDto());
		//then
		verify(repository).save(task);
		verify(mapper).toDto(task);
		assertEquals("Updated name", updatedTask.getName());
		assertEquals("Updated description", updatedTask.getDescription());
	}

	@Test
	void shouldThrowAnExceptionWhenTryingToUpdateNonExistingTask() {
		//given
		when(repository.findByIdAndUser(any(), any())).thenReturn(Optional.empty());
		//when
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.updateTask(1L, createUpdateTaskDto()));
		//then
		assertEquals("Task with ID [1] not found", exception.getMessage());
		verify(repository, never()).save(any());
		verify(mapper, never()).toDto(any(Task.class));
	}

	@Test
	void shouldFindTask() {
		//given
		when(repository.findByIdAndUserWithSubtasks(any(), any())).thenReturn(Optional.of(createTask()));
		//when
		service.findTask(1L);
		//then
		verify(mapper).toDto(any(Task.class));
	}

	@Test
	void shouldThrowAnExceptionWhenTryingToFindNonExistingTask() {
		//given
		when(repository.findByIdAndUserWithSubtasks(any(), any())).thenReturn(Optional.empty());
		//when
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.findTask(1L));
		//then
		assertEquals("Task with ID [1] not found", exception.getMessage());
		verify(mapper, never()).toDto(any(Task.class));
	}

	@Test
	void shouldFindTasks() {
		//given
		Task task1 = createTask();
		Task task2 = createTask();
		task2.setId(2L);
		PageImpl<Task> taskDtos = new PageImpl<>(List.of(task1, task2));
		TaskDto taskDto1 = new TaskDto();
		TaskDto taskDto2 = new TaskDto();
		when(repository.findAllByUser(any(), any())).thenReturn(taskDtos);
		when(mapper.toDto(task1)).thenReturn(taskDto1);
		when(mapper.toDto(task2)).thenReturn(taskDto2);
		//when
		Page<TaskDto> page = service.findTasks(Optional.of(0), Optional.of(10), Optional.of("id"));
		//then
		verify(mapper, times(2)).toDto(any(Task.class));
		assertTrue(page.hasContent());
		assertEquals(2, page.getTotalElements());
		assertTrue(page.getContent().contains(taskDto1));
		assertTrue(page.getContent().contains(taskDto2));
	}

	@Test
	void shouldNotFindTasks() {
		//given
		when(repository.findAllByUser(any(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
		//when
		Page<TaskDto> page = service.findTasks(Optional.of(0), Optional.of(10), Optional.of("id"));
		//then
		verify(mapper, never()).toDto(any(Task.class));
		assertFalse(page.hasContent());
		assertEquals(0, page.getTotalElements());
	}

	private UpdateTaskDto createUpdateTaskDto() {
		UpdateTaskDto updateTaskDto = new UpdateTaskDto();
		updateTaskDto.setName("Updated Task");
		updateTaskDto.setDescription("Updated Description");
		updateTaskDto.setStartDate("01/01/2029 08:00:00");
		updateTaskDto.setFinishDate("01/01/2029 09:00:00");
		return updateTaskDto;
	}

	private Task createTask() {
		Task task = new Task();
		task.setId(1L);
		task.setName("Test name");
		task.setDescription("Test description");
		task.setStartDate(DateHelper.parseStringToZonedDateTime("01/01/2028 08:00:00"));
		task.setFinishDate(DateHelper.parseStringToZonedDateTime("01/01/2028 09:00:00"));
		task.setStatus(TaskStatus.PLANNED);
		return task;
	}
}