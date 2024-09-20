package pl.zarczynski.usm.task;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import pl.zarczynski.usm.common.DateHelper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@Service
public class DtoValidator {

	public void validate (CreateTaskDto createTaskDto) {
		if (createTaskDto.getName() == null || createTaskDto.getName().isEmpty()) {
			throw new ValidationException("Name must not be null or empty");
		}
		if (createTaskDto.getDescription() == null || createTaskDto.getDescription().isEmpty()) {
			throw new ValidationException("Description must not be null or empty");
		}
		if (createTaskDto.getStartDate() == null || createTaskDto.getStartDate().isEmpty()) {
			throw new ValidationException("StartDate must not be null or empty");
		}
		if (createTaskDto.getFinishDate() == null || createTaskDto.getFinishDate().isEmpty()) {
			throw new ValidationException("FinishDate must not be null or empty");
		}
		try {
			ZonedDateTime startDate = DateHelper.parseStringToZonedDateTime(createTaskDto.getStartDate());
			ZonedDateTime finishDate = DateHelper.parseStringToZonedDateTime(createTaskDto.getFinishDate());
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
