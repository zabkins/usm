package pl.zarczynski.usm.task.service;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.task.dto.CreateTaskDto;
import pl.zarczynski.usm.task.dto.UpdateTaskDto;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@Service
public class DtoValidator {

	public void validate (CreateTaskDto dto) {
		validateNotNullAndNotEmpty(dto.getName(), "name");
		validateNotNullAndNotEmpty(dto.getDescription(), "description");
		validateNotNullAndNotEmpty(dto.getStartDate(), "startDate");
		validateNotNullAndNotEmpty(dto.getFinishDate(), "finishDate");
		validateDates(dto.getStartDate(), dto.getFinishDate());
	}

	public void validate (UpdateTaskDto dto) {
		validateNotNullAndNotEmpty(dto.getName(), "name");
		validateNotNullAndNotEmpty(dto.getDescription(), "description");
		validateNotNullAndNotEmpty(dto.getStartDate(), "startDate");
		validateNotNullAndNotEmpty(dto.getFinishDate(), "finishDate");
		validateNotNull(dto.getStatus(), "status");
		validateDates(dto.getStartDate(), dto.getFinishDate());
	}

	private void validateNotNullAndNotEmpty(String fieldValue, String fieldName) {
		if (fieldValue == null || fieldValue.isEmpty()) {
			throw new ValidationException(fieldName + " must not be null or empty");
		}
	}

	private void validateNotNull(Object fieldValue, String fieldName) {
		if (fieldValue == null) {
			throw new ValidationException(fieldName + " must not be null or empty");
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
}
