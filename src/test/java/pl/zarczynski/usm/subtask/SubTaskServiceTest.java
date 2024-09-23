package pl.zarczynski.usm.subtask;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.common.TaskMapper;
import pl.zarczynski.usm.security.WithMockCustomUser;
import pl.zarczynski.usm.subtask.dto.CreateSubTaskDto;
import pl.zarczynski.usm.subtask.dto.SubTaskDto;
import pl.zarczynski.usm.subtask.dto.UpdateSubTaskDto;
import pl.zarczynski.usm.subtask.entity.SubTask;
import pl.zarczynski.usm.task.TaskRepository;
import pl.zarczynski.usm.task.dto.CreateTaskDto;
import pl.zarczynski.usm.task.entity.Task;
import pl.zarczynski.usm.task.entity.TaskStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WithMockCustomUser
class SubTaskServiceTest {

	@InjectMocks
	private SubTaskService service;
	@Mock
	private SubTaskRepository subTaskRepository;
	@Mock
	private TaskRepository taskRepository;
	@Mock
	private TaskMapper mapper;

	@Test
	void shouldSaveSubTask () {
		//given
		when(taskRepository.findByIdAndUserWithSubtasks(any(), any())).thenReturn(Optional.of(createTask()));
		when(mapper.fromDto(any(CreateSubTaskDto.class))).thenReturn(new SubTask());
		when(subTaskRepository.save(any(SubTask.class))).thenReturn(createSubTask());
		CreateSubTaskDto createSubTaskDto = new CreateSubTaskDto();
		//when
		service.saveSubTask(1L, createSubTaskDto);
		//then
		verify(taskRepository).findByIdAndUserWithSubtasks(eq(1L), any());
		verify(mapper).fromDto(createSubTaskDto);
		verify(subTaskRepository).save(any(SubTask.class));
		verify(taskRepository).save(any(Task.class));
		verify(mapper).toDto(any(SubTask.class));
	}

	@Test
	void shouldNotThrowExceptionWhenTaskFound () {
		//given
		when(subTaskRepository.findSubTaskByIdAndUser(eq(1L), any())).thenReturn(Optional.of(createSubTask()));
		//when
		assertDoesNotThrow(() -> service.findSubTask(1L));
		//then
		verify(mapper).toDto(any(SubTask.class));
	}

	@Test
	void shouldThrowExceptionWhenTaskNotFound () {
		//given
		when(subTaskRepository.findSubTaskByIdAndUser(eq(1L), any())).thenReturn(Optional.empty());
		//when
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.findSubTask(1L));
		//then
		assertEquals("SubTask with ID [1] not found", exception.getMessage());
		verify(mapper, never()).toDto(any(SubTask.class));
	}

	@Test
	void shouldDeleteSubTaskWhenFound () {
		//given
		SubTask subTask = createSubTask();
		when(subTaskRepository.findSubTaskByIdAndUser(eq(1L), any())).thenReturn(Optional.of(subTask));
		//when
		assertDoesNotThrow(() -> service.deleteSubTask(1L));
		//then
		verify(subTaskRepository).delete(subTask);
	}

	@Test
	void shouldThrowWhenTryingToDeleteNonExistingSubTask () {
		//given
		when(subTaskRepository.findSubTaskByIdAndUser(eq(1L), any())).thenReturn(Optional.empty());
		//when
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.deleteSubTask(1L));
		//then
		assertEquals("SubTask with ID [1] not found", exception.getMessage());
		verify(subTaskRepository, never()).delete(any());
	}

	@Test
	void shouldUpdateSubTaskWhenFound () {
		//given
		SubTask subTask = createSubTask();
		when(subTaskRepository.findSubTaskByIdAndUser(any(), any())).thenReturn(Optional.of(subTask));
		when(subTaskRepository.save(subTask)).thenReturn(new SubTask());
		SubTaskDto updatedSubTaskDto = new SubTaskDto();
		updatedSubTaskDto.setId(1L);
		updatedSubTaskDto.setName("UpdatedName");
		updatedSubTaskDto.setDescription("UpdatedDescription");
		when(mapper.toDto(any(SubTask.class))).thenReturn(updatedSubTaskDto);
		//when
		SubTaskDto subTaskDto = service.updateSubTask(1L, new UpdateSubTaskDto());
		//then
		verify(subTaskRepository).findSubTaskByIdAndUser(any(), any());
		verify(subTaskRepository).save(subTask);
		verify(mapper).toDto(any(SubTask.class));
		assertEquals("UpdatedName", subTaskDto.getName());
		assertEquals("UpdatedDescription", subTaskDto.getDescription());
	}

	@Test
	void shouldThrowWhenTryingToUpdateNonExistingSubTask () {
		//given
		when(subTaskRepository.findSubTaskByIdAndUser(any(), any())).thenReturn(Optional.empty());
		//when
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.updateSubTask(1L, new UpdateSubTaskDto()));
		//then
		assertEquals("SubTask with ID [1] not found", exception.getMessage());
		verify(subTaskRepository, never()).save(any());
		verify(mapper, never()).toDto(any(SubTask.class));
	}

	@Test
	void shouldFindAllSubtasks () {
		//given
		SubTask subTask = createSubTask();
		SubTask subTask2 = createSubTask();
		subTask2.setId(2L);
		when(subTaskRepository.findAllSubTasksForGivenTask(eq(1L), any())).thenReturn(List.of(subTask, subTask2));
		when(mapper.toDto(any(SubTask.class))).thenReturn(new SubTaskDto());
		//when
		List<SubTaskDto> foundSubTasks = service.findSubTasksForGivenTask(1L);
		//then
		assertFalse(foundSubTasks.isEmpty());
	}

	@Test
	void shouldNotFindAnySubTasks () {
		//given
		when(subTaskRepository.findAllSubTasksForGivenTask(eq(1L), any())).thenReturn(Collections.emptyList());
		//when
		List<SubTaskDto> foundSubTasks = service.findSubTasksForGivenTask(1L);
		//then
		assertTrue(foundSubTasks.isEmpty());
		verify(mapper, never()).toDto(any(SubTask.class));
	}

	private SubTask createSubTask () {
		SubTask subTask = new SubTask();
		subTask.setId(1L);
		subTask.setName("name");
		subTask.setDescription("description");
		subTask.setDone(false);
		return subTask;
	}

	private Task createTask () {
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