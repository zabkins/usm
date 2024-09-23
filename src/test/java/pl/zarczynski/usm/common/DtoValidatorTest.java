package pl.zarczynski.usm.common;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import pl.zarczynski.usm.authentication.dto.RegisterUserRequest;
import pl.zarczynski.usm.subtask.dto.CreateSubTaskDto;
import pl.zarczynski.usm.subtask.dto.UpdateSubTaskDto;
import pl.zarczynski.usm.task.dto.CreateTaskDto;
import pl.zarczynski.usm.task.dto.UpdateTaskDto;
import pl.zarczynski.usm.task.entity.TaskStatus;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DtoValidatorTest {


	private final DtoValidator dtoValidator = new DtoValidator();

	@Test
	public void shouldThrowAnExceptionWhenRegisterUserRequestInvalid_1() {
		//given
		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		//when
		BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> dtoValidator.validate(registerUserRequest));
		//then
		assertEquals("Email is required", exception.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenRegisterUserRequestInvalid_2() {
		//given
		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		registerUserRequest.setEmail("testuser.gmail.com");
		//when
		BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> dtoValidator.validate(registerUserRequest));
		//then
		assertEquals("Invalid email format", exception.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenRegisterUserRequestInvalid_3() {
		//given
		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		registerUserRequest.setEmail("testuser@gmail.com");
		//when
		BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> dtoValidator.validate(registerUserRequest));
		//then
		assertEquals("Password is required", exception.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenRegisterUserRequestInvalid_4() {
		//given
		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		registerUserRequest.setEmail("testuser@gmail.com");
		registerUserRequest.setPassword("password");
		//when
		BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> dtoValidator.validate(registerUserRequest));
		//then
		assertEquals("Full name is required", exception.getMessage());
	}

	@Test
	public void shouldNotThrowAnExceptionWhenRegisterUserRequestValid() {
		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		registerUserRequest.setEmail("testuser@gmail.com");
		registerUserRequest.setPassword("password");
		registerUserRequest.setFullName("Full Name");
		//when
		assertDoesNotThrow(() -> dtoValidator.validate(registerUserRequest));
		//then
	}

	@Test
	public void shouldThrowAnExceptionWhenCreateTaskDtoInvalid_1() {
		//given
		CreateTaskDto dto = new CreateTaskDto();
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("name must not be null", validationException.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenCreateTaskDtoInvalid_2() {
		//given
		CreateTaskDto dto = new CreateTaskDto();
		dto.setName("Task name");
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("description must not be null", validationException.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenCreateTaskDtoInvalid_3() {
		//given
		CreateTaskDto dto = new CreateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("startDate must not be null", validationException.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenCreateTaskDtoInvalid_4() {
		//given
		CreateTaskDto dto = new CreateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		dto.setStartDate("25/05/2025 16:00:00.000");
		dto.setFinishDate("25/05/2025 16:00:00");
		//when
		ValidationException exception = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("Invalid startDate/finishDate format. Expected: " + DateHelper.dtoDateFormat, exception.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenCreateTaskDtoInvalid_5() {
		//given
		CreateTaskDto dto = new CreateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		String nowAsString = DateHelper.parseDate(ZonedDateTime.now());
		String formattedStartDate = nowAsString.substring(0, nowAsString.lastIndexOf(" "));
		dto.setStartDate(formattedStartDate);
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("finishDate must not be null", validationException.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenCreateTaskDtoInvalid_6() {
		//given
		CreateTaskDto dto = new CreateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		dto.setStartDate("25/05/2025 16:00:00");
		dto.setFinishDate("25/05/2025 16:00:00.000");
		//when
		ValidationException exception = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("Invalid startDate/finishDate format. Expected: " + DateHelper.dtoDateFormat, exception.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenCreateTaskDtoInvalid_7() {
		//given
		CreateTaskDto dto = new CreateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		String startDate = DateHelper.parseDate(ZonedDateTime.now().minusYears(1));
		String finishDate = DateHelper.parseDate(ZonedDateTime.now().minusDays(1));
		String formattedStartDate = startDate.substring(0, startDate.lastIndexOf(" "));
		String formattedFinishDate = finishDate.substring(0, finishDate.lastIndexOf(" "));
		dto.setStartDate(formattedStartDate);
		dto.setFinishDate(formattedFinishDate);
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("FinishDate cannot be in the past", validationException.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenCreateTaskDtoInvalid_8() {
		//given
		CreateTaskDto dto = new CreateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		String startDate = DateHelper.parseDate(ZonedDateTime.now().plusDays(10));
		String finishDate = DateHelper.parseDate(ZonedDateTime.now().plusDays(5));
		String formattedStartDate = startDate.substring(0, startDate.lastIndexOf(" "));
		String formattedFinishDate = finishDate.substring(0, finishDate.lastIndexOf(" "));
		dto.setStartDate(formattedStartDate);
		dto.setFinishDate(formattedFinishDate);
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("StartDate must be before FinishDate", validationException.getMessage());
	}

	@Test
	public void shouldNotThrowAnExceptionWhenCreateTaskDtoValid() {
		//given
		CreateTaskDto dto = new CreateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		String startDate = DateHelper.parseDate(ZonedDateTime.now());
		String finishDate = DateHelper.parseDate(ZonedDateTime.now().plusWeeks(1));
		String formattedStartDate = startDate.substring(0, startDate.lastIndexOf(" "));
		String formattedFinishDate = finishDate.substring(0, finishDate.lastIndexOf(" "));
		dto.setStartDate(formattedStartDate);
		dto.setFinishDate(formattedFinishDate);
		//then
		assertDoesNotThrow(() -> dtoValidator.validate(dto));
	}


	@Test
	public void shouldThrowAnExceptionWhenUpdateTaskDtoInvalid_1() {
		//given
		UpdateTaskDto dto = new UpdateTaskDto();
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("name must not be null", validationException.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenUpdateTaskDtoInvalid_2() {
		//given
		UpdateTaskDto dto = new UpdateTaskDto();
		dto.setName("Task name");
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("description must not be null", validationException.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenUpdateTaskDtoInvalid_3() {
		//given
		UpdateTaskDto dto = new UpdateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("startDate must not be null", validationException.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenUpdateTaskDtoInvalid_4() {
		//given
		UpdateTaskDto dto = new UpdateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		String nowAsString = DateHelper.parseDate(ZonedDateTime.now());
		String formattedStartDate = nowAsString.substring(0, nowAsString.lastIndexOf(" "));
		dto.setStartDate(formattedStartDate);
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("finishDate must not be null", validationException.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenUpdateTaskDtoInvalid_5() {
		//given
		UpdateTaskDto dto = new UpdateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		String startDate = DateHelper.parseDate(ZonedDateTime.now());
		String finishDate = DateHelper.parseDate(ZonedDateTime.now().plusWeeks(1));
		String formattedStartDate = startDate.substring(0, startDate.lastIndexOf(" "));
		String formattedFinishDate = finishDate.substring(0, finishDate.lastIndexOf(" "));
		dto.setStartDate(formattedStartDate);
		dto.setFinishDate(formattedFinishDate);
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("status must not be null", validationException.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenUpdateTaskDtoInvalid_6() {
		//given
		UpdateTaskDto dto = new UpdateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		dto.setStartDate("25/05/2025 16:00:00.000");
		dto.setFinishDate("25/05/2025 16:00:00");
		dto.setStatus(TaskStatus.PLANNED);
		//when
		ValidationException exception = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("Invalid startDate/finishDate format. Expected: " + DateHelper.dtoDateFormat, exception.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenUpdateTaskDtoInvalid_7() {
		//given
		UpdateTaskDto dto = new UpdateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		dto.setStartDate("25/05/2025 16:00:00");
		dto.setFinishDate("25/05/2025 16:00:00.000");
		dto.setStatus(TaskStatus.PLANNED);
		//when
		ValidationException exception = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("Invalid startDate/finishDate format. Expected: " + DateHelper.dtoDateFormat, exception.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenUpdateTaskDtoInvalid_8() {
		//given
		UpdateTaskDto dto = new UpdateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		String startDate = DateHelper.parseDate(ZonedDateTime.now().minusYears(1));
		String finishDate = DateHelper.parseDate(ZonedDateTime.now().minusDays(1));
		String formattedStartDate = startDate.substring(0, startDate.lastIndexOf(" "));
		String formattedFinishDate = finishDate.substring(0, finishDate.lastIndexOf(" "));
		dto.setStartDate(formattedStartDate);
		dto.setFinishDate(formattedFinishDate);
		dto.setStatus(TaskStatus.PLANNED);
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("FinishDate cannot be in the past", validationException.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenUpdateTaskDtoInvalid_9() {
		//given
		UpdateTaskDto dto = new UpdateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		String startDate = DateHelper.parseDate(ZonedDateTime.now().plusDays(10));
		String finishDate = DateHelper.parseDate(ZonedDateTime.now().plusDays(5));
		String formattedStartDate = startDate.substring(0, startDate.lastIndexOf(" "));
		String formattedFinishDate = finishDate.substring(0, finishDate.lastIndexOf(" "));
		dto.setStartDate(formattedStartDate);
		dto.setFinishDate(formattedFinishDate);
		dto.setStatus(TaskStatus.PLANNED);
		//when
		ValidationException validationException = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("StartDate must be before FinishDate", validationException.getMessage());
	}

	@Test
	public void shouldNotThrowAnExceptionWhenUpdateTaskDtoValid() {
		//given
		UpdateTaskDto dto = new UpdateTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		String startDate = DateHelper.parseDate(ZonedDateTime.now());
		String finishDate = DateHelper.parseDate(ZonedDateTime.now().plusWeeks(1));
		String formattedStartDate = startDate.substring(0, startDate.lastIndexOf(" "));
		String formattedFinishDate = finishDate.substring(0, finishDate.lastIndexOf(" "));
		dto.setStartDate(formattedStartDate);
		dto.setFinishDate(formattedFinishDate);
		dto.setStatus(TaskStatus.PLANNED);
		//then
		assertDoesNotThrow(() -> dtoValidator.validate(dto));
	}

	@Test
	public void shouldThrowAnExceptionWhenCreateSubTaskDtoValid_1() {
		//given
		CreateSubTaskDto dto = new CreateSubTaskDto();
		//when
		ValidationException exception = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("name must not be null", exception.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenCreateSubTaskDtoValid_2() {
		//given
		CreateSubTaskDto dto = new CreateSubTaskDto();
		dto.setName("name");
		//when
		ValidationException exception = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("description must not be null", exception.getMessage());
	}

	@Test
	public void shouldNotThrowAnExceptionWhenCreateSubTaskDtoValid() {
		//given
		UpdateSubTaskDto dto = new UpdateSubTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		//then
		assertDoesNotThrow(() -> dtoValidator.validate(dto));
	}

	@Test
	public void shouldThrowAnExceptionWhenUpdateSubTaskDtoValid_1() {
		//given
		UpdateSubTaskDto dto = new UpdateSubTaskDto();
		//when
		ValidationException exception = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("name must not be null", exception.getMessage());
	}

	@Test
	public void shouldThrowAnExceptionWhenUpdateSubTaskDtoValid_2() {
		//given
		UpdateSubTaskDto dto = new UpdateSubTaskDto();
		dto.setName("name");
		//when
		ValidationException exception = assertThrows(ValidationException.class, () -> dtoValidator.validate(dto));
		//then
		assertEquals("description must not be null", exception.getMessage());
	}

	@Test
	public void shouldNotThrowAnExceptionWhenUpdateSubTaskDtoValid() {
		//given
		UpdateSubTaskDto dto = new UpdateSubTaskDto();
		dto.setName("name");
		dto.setDescription("description");
		//then
		assertDoesNotThrow(() -> dtoValidator.validate(dto));
	}
}