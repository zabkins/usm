package pl.zarczynski.usm.authentication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.zarczynski.usm.authentication.dto.LoginUserRequest;
import pl.zarczynski.usm.authentication.dto.RegisterUserRequest;
import pl.zarczynski.usm.configuration.user.User;
import pl.zarczynski.usm.configuration.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

	@InjectMocks
	private AuthenticationService authenticationService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private AuthenticationManager authenticationManager;

	@Test
	void shouldSaveUserWhenNoUserWithGivenEmailFound() {
		//given
		RegisterUserRequest dto = createDefaultRegisterUserRequest();
		when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
		when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
		when(userRepository.save(any())).thenReturn(new User());
		//when
		User user = authenticationService.signUp(dto);
		//then
		verify(passwordEncoder).encode(dto.getPassword());
		verify(userRepository, atLeast(1)).findByEmail(dto.getEmail());
		verify(userRepository).save(any());
	}

	@Test
	void shouldThrowExceptionWhenUserWithGivenEmailExists() {
		//given
		RegisterUserRequest dto = createDefaultRegisterUserRequest();
		when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
		when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
		//when
		BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
			authenticationService.signUp(dto);
		});
		//then
		assertEquals("Email is already in use", exception.getMessage());
	}

	@Test
	void shouldAuthenticateWhenValidData() {
		//given
		LoginUserRequest dto = createDefaultUserLoginRequest();
		when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(createDefaultUser()));
		//when
		User user = authenticationService.authenticate(dto);
		//then
		assertNotNull(user);
	}

	@Test
	void shouldThrowExceptionWhenUserNotFound() {
		//given
		LoginUserRequest dto = createDefaultUserLoginRequest();
		//when
		UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
			authenticationService.authenticate(dto);
		});
		//then
		assertEquals("User " + dto.getEmail() + " not found", exception.getMessage());
	}

	private User createDefaultUser() {
		User user = new User();
		user.setEmail("email@email.com");
		user.setPassword("password");
		return user;
	}

	private LoginUserRequest createDefaultUserLoginRequest() {
		LoginUserRequest dto = new LoginUserRequest();
		dto.setEmail("testuser@gmail.com");
		dto.setPassword("password");
		return dto;
	}

	private RegisterUserRequest createDefaultRegisterUserRequest () {
		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		registerUserRequest.setEmail("testuser@gmail.com");
		registerUserRequest.setPassword("password");
		registerUserRequest.setFullName("Test User");
		return registerUserRequest;
	}
}