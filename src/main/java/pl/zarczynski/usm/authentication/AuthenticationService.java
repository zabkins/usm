package pl.zarczynski.usm.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.zarczynski.usm.authentication.dto.LoginUserRequest;
import pl.zarczynski.usm.authentication.dto.RegisterUserRequest;
import pl.zarczynski.usm.configuration.user.User;
import pl.zarczynski.usm.configuration.user.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	public User signUp (RegisterUserRequest dto) {
		User user = new User();
		user.setFullName(dto.getFullName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
			log.error("User with email {} already exists", dto.getEmail());
			throw new BadCredentialsException("Email is already in use");
		}
		return userRepository.save(user);
	}

	public User authenticate (LoginUserRequest dto) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
		return userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new UsernameNotFoundException(dto.getEmail()));
	}
}
