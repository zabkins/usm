package pl.zarczynski.usm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zarczynski.usm.authentication.UserInfoDto;
import pl.zarczynski.usm.configuration.jwt.JwtService;
import pl.zarczynski.usm.configuration.user.User;

import java.util.Date;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final JwtService jwtService;

	@GetMapping("/me")
	public ResponseEntity<UserInfoDto> authenticatedUser (@RequestHeader("Authorization") String authHeader) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		log.info("Preparing information about user {}", currentUser.getUsername());
		Date expirationDate = jwtService.extractExpiration(authHeader.substring(7));
		long secondsUntilExpiration = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
		UserInfoDto dto = UserInfoDto.builder()
				.name(currentUser.getFullName())
				.email(currentUser.getEmail())
				.createdAt(currentUser.getCreatedAt().toString())
				.isExpired(!currentUser.isAccountNonExpired())
				.expiration(expirationDate + ". Valid for: " + secondsUntilExpiration + "s")
				.build();
		log.info("Returning information about user {}", dto);
		return ResponseEntity.ok(dto);
	}
}

