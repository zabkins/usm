package pl.zarczynski.usm.task.service;

import jakarta.validation.ValidationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import pl.zarczynski.usm.authentication.RegisterUserRequest;
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.task.dto.CreateTaskDto;
import pl.zarczynski.usm.task.dto.UpdateTaskDto;
import pl.zarczynski.usm.subtask.dto.CreateSubTaskDto;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@Service
public class DtoValidator {

	private final String EMAIL_PATTERN = "^(.+)@(\\S+)$";

	public void validate (CreateTaskDto dto) {
		validateNotNull(dto.getName(), "name");
		validateNotNull(dto.getDescription(), "description");
		validateNotNull(dto.getStartDate(), "startDate");
		validateNotNull(dto.getFinishDate(), "finishDate");
		validateDates(dto.getStartDate(), dto.getFinishDate());
	}

	public void validate (UpdateTaskDto dto) {
		validateNotNull(dto.getName(), "name");
		validateNotNull(dto.getDescription(), "description");
		validateNotNull(dto.getStartDate(), "startDate");
		validateNotNull(dto.getFinishDate(), "finishDate");
		validateNotNull(dto.getStatus(), "status");
		validateDates(dto.getStartDate(), dto.getFinishDate());
	}

	public void validate (CreateSubTaskDto subTaskDto) {
		validateNotNull(subTaskDto.getName(), "name");
		validateNotNull(subTaskDto.getDescription(), "description");
	}

	private void validateNotNull(Object fieldValue, String fieldName) {
		if (fieldValue == null) {
			throw new ValidationException(fieldName + " must not be null");
		}
	}

	private void validateDates(String startDateString, String finishDateString) {
		try {
			ZonedDateTime startDate = DateHelper.parseStringToZonedDateTime(startDateString);
			ZonedDateTime finishDate = DateHelper.parseStringToZonedDateTime(finishDateString);
			if (finishDate.isBefore(ZonedDateTime.now())) {
				throw new ValidationException("FinishDate cannot be in the past");
			}
			if (finishDate.isBefore(startDate)) {
				throw new ValidationException("StartDate must be before FinishDate");
			}
		} catch (DateTimeParseException e) {
			throw new ValidationException("Invalid startDate/finishDate format. Expected: " + DateHelper.dtoDateFormat);
		}
	}

	public void validate (RegisterUserRequest dto) {
		if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
			throw new BadCredentialsException("Email is required");
		}
		if (!Pattern.matches(EMAIL_PATTERN, dto.getEmail())) {
			throw new BadCredentialsException("Invalid email format");
		}
		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new BadCredentialsException("Password is required");
		}
		if (dto.getFullName() == null || dto.getFullName().isEmpty()) {
			throw new BadCredentialsException("Full name is required");
		}
	}
}
