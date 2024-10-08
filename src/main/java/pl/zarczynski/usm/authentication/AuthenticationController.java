package pl.zarczynski.usm.authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import pl.zarczynski.usm.authentication.dto.LoginUserRequest;
import pl.zarczynski.usm.authentication.dto.LoginUserResponse;
import pl.zarczynski.usm.authentication.dto.RegisterUserRequest;
import pl.zarczynski.usm.authentication.dto.RegisterUserResponse;
import pl.zarczynski.usm.configuration.jwt.JwtService;
import pl.zarczynski.usm.configuration.user.User;
import pl.zarczynski.usm.swaggerschemas.auth.BadCredentialsProblemDetailSchema;
import pl.zarczynski.usm.swaggerschemas.auth.EmailInUseProblemDetailSchema;
import pl.zarczynski.usm.swaggerschemas.auth.InvalidEmailProblemDetailSchema;
import pl.zarczynski.usm.swaggerschemas.auth.InvalidJwtTokenProblemDetailSchema;
import pl.zarczynski.usm.common.DtoValidator;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {
	private final AuthenticationService authenticationService;
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final DtoValidator dtoValidator;

	@PostMapping("/signup")
	@Operation(description = "Register user")
	@ApiResponses({
			@ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = RegisterUserResponse.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = EmailInUseProblemDetailSchema.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = InvalidEmailProblemDetailSchema.class), mediaType = "application/json")})
	})
	public ResponseEntity<RegisterUserResponse> registerUser (@RequestBody RegisterUserRequest dto) {
		dtoValidator.validate(dto);
		log.info("Registering user: {}", dto);
		User registeredUser = authenticationService.signUp(dto);
		RegisterUserResponse response = new RegisterUserResponse(registeredUser);
		log.info("Registered user: {}", response);
		return ResponseEntity.status(201).body(response);
	}

	@PostMapping("/login")
	@Operation(description = "Login")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = LoginUserResponse.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = BadCredentialsProblemDetailSchema.class), mediaType = "application/json")),
	})
	public ResponseEntity<LoginUserResponse> loginUser (@RequestBody LoginUserRequest dto) {
		log.info("Authenticating user: {}", dto.getEmail());
		User authenticatedUser = authenticationService.authenticate(dto);
		String jwtToken = jwtService.generateToken(authenticatedUser);
		LoginUserResponse loginUserResponse = new LoginUserResponse(jwtToken, jwtService.getJwtExpirationTime());
		log.info("Authenticated user: {}", authenticatedUser);
		return ResponseEntity.ok(loginUserResponse);
	}

	@PostMapping("/refresh")
	@Operation(description = "Refresh user token")
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = LoginUserResponse.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = InvalidJwtTokenProblemDetailSchema.class), mediaType = "application/json")),
	})
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<LoginUserResponse> refreshToken (@RequestHeader("Authorization") @Parameter(hidden = true) String authHeader) {
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
