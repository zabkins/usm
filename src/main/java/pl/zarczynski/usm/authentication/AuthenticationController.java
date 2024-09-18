package pl.zarczynski.usm.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zarczynski.usm.configuration.jwt.JwtService;
import pl.zarczynski.usm.configuration.user.User;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
	private final AuthenticationService authenticationService;
	private final JwtService jwtService;

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
}
