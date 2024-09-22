package pl.zarczynski.usm.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler(value = {Exception.class})
	public ProblemDetail handleSecurityException (Exception exception) {
		ProblemDetail errorDetail = null;
		log.error("Error occured [{}]. StackTrace: {}", exception.getMessage(), exception.getStackTrace());
		if (exception instanceof EntityNotFoundException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());
			errorDetail.setProperty("description", "Resource not found");
		}
		if (exception instanceof ValidationException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
			errorDetail.setProperty("description", "Request input is invalid");
		}
		if (exception instanceof BadCredentialsException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
			errorDetail.setProperty("description", "The username or password is incorrect");

			return errorDetail;
		}

		if (exception instanceof AccountStatusException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
			errorDetail.setProperty("description", "The account is locked");
		}

		if (exception instanceof AccessDeniedException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
			errorDetail.setProperty("description", "You are not authorized to access this resource");
		}

		if (exception instanceof SignatureException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
			errorDetail.setProperty("description", "The JWT signature is invalid");
		}

		if (exception instanceof ExpiredJwtException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
			errorDetail.setProperty("description", "The JWT token has expired");
		}

		if (errorDetail == null) {
			if (exception.getMessage().equals("Invalid JWT Token")){
				errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
				errorDetail.setProperty("description", "JWT provided in the request is invalid");
				return errorDetail;
			}
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
			errorDetail.setProperty("description", "Unknown internal server error.");
		}

		return errorDetail;
	}
}
