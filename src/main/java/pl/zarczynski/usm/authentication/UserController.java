package pl.zarczynski.usm.authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.configuration.jwt.JwtService;
import pl.zarczynski.usm.configuration.user.User;
import pl.zarczynski.usm.exceptions.ProblemDetailSchema;

import java.util.Date;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "Token holder's information")
public class UserController {

	private final JwtService jwtService;

	@GetMapping("/me")
	@Operation(description = "Get information about currently authenticated user")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserInfoDto.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ProblemDetailSchema.class), mediaType = "application/json")),
	})
	public ResponseEntity<UserInfoDto> authenticatedUser (@RequestHeader("Authorization") String authHeader) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		log.info("Preparing information about user {}", currentUser.getUsername());
		Date expirationDate = jwtService.extractExpiration(authHeader.substring(7));
		long secondsUntilExpiration = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
		UserInfoDto dto = UserInfoDto.builder()
				.name(currentUser.getFullName())
				.email(currentUser.getEmail())
				.createdAt(DateHelper.parseDate(currentUser.getCreatedAt()))
				.updatedAt(DateHelper.parseDate(currentUser.getUpdatedAt()))
				.isExpired(!currentUser.isAccountNonExpired())
				.expiration(DateHelper.parseDate(expirationDate) + ". Valid for: " + secondsUntilExpiration + "s")
				.build();
		log.info("Returning information about user {}", dto);
		return ResponseEntity.ok(dto);
	}
}

