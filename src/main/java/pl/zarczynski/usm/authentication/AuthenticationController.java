package pl.zarczynski.usm.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import pl.zarczynski.usm.configuration.jwt.JwtService;
import pl.zarczynski.usm.configuration.user.User;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
	private final AuthenticationService authenticationService;
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@PostMapping("/signup")
	public ResponseEntity<RegisterUserResponse> registerUser (@RequestBody RegisterUserRequest dto) {
		log.info("Registering user: {}", dto);
		User registeredUser = authenticationService.signUp(dto);
		RegisterUserResponse response = new RegisterUserResponse(registeredUser);
		log.info("Registered user: {}", response);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginUserResponse> loginUser (@RequestBody LoginUserRequest dto) {
		log.info("Authenticating user: {}", dto.getEmail());
		User authenticatedUser = authenticationService.authenticate(dto);
		String jwtToken = jwtService.generateToken(authenticatedUser);
		LoginUserResponse loginUserResponse = new LoginUserResponse(jwtToken, jwtService.getJwtExpirationTime());
		log.info("Authenticated user: {}", authenticatedUser);
		return ResponseEntity.ok(loginUserResponse);
	}

	@PostMapping("/refresh")
	public ResponseEntity<LoginUserResponse> refreshToken (@RequestHeader("Authorization") String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new BadCredentialsException("Invalid token");
		}

		String token = authHeader.substring(7);
		String userName = jwtService.extractUsername(token);
		log.info("Refreshing token for User {}", userName);
		if (userName != null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
			if (jwtService.isTokenValid(token, userDetails)) {
				String refreshedToken = jwtService.refreshToken(token);
				LoginUserResponse loginUserResponse = new LoginUserResponse(refreshedToken, jwtService.getJwtExpirationTime());
				log.info("Refreshed token for User {} successfully.", userName);
				return ResponseEntity.ok(loginUserResponse);
			}
		}
		log.error("Token refresh for user {} unsuccessful", userName);
		throw new BadCredentialsException("Invalid token");
	}

}
