package pl.zarczynski.usm.authentication;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
public class AuthenticationController {
	private final AuthenticationService authenticationService;
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@PostMapping("/signup")
	public ResponseEntity<User> registerUser (@RequestBody RegisterUserDto dto) {
		User registeredUser = authenticationService.signUp(dto);
		return ResponseEntity.ok(registeredUser);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> loginUser (@RequestBody LoginUserDto dto) {
		User authenticatedUser = authenticationService.authenticate(dto);
		String jwtToken = jwtService.generateToken(authenticatedUser);
		LoginResponse loginResponse = LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getJwtExpirationTime()).build();
		return ResponseEntity.ok(loginResponse);
	}

	@PostMapping("/refresh")
	public ResponseEntity<LoginResponse> refreshToken (@RequestHeader("Authorization") String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new BadCredentialsException("Invalid token");
		}

		String token = authHeader.substring(7);
		String userName = jwtService.extractUsername(token);
		if (userName != null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
			if (jwtService.isTokenValid(token, userDetails)) {
				String refreshedToken = jwtService.refreshToken(token);
				LoginResponse loginResponse = LoginResponse.builder().token(refreshedToken).expiresIn(jwtService.getJwtExpirationTime()).build();
				return ResponseEntity.ok(loginResponse);
			}
		}
		throw new BadCredentialsException("Invalid token");
	}

}
