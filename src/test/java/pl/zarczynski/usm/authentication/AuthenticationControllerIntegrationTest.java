package pl.zarczynski.usm.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.zarczynski.usm.authentication.dto.LoginUserRequest;
import pl.zarczynski.usm.authentication.dto.LoginUserResponse;
import pl.zarczynski.usm.authentication.dto.RegisterUserRequest;
import pl.zarczynski.usm.configuration.user.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AuthenticationControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserRepository userRepository;

	@Test
	public void shouldNotAuthenticate() throws Exception {
		mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createUserLoginRequest("nonexistentuser@gmail.com", "password")))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(jsonPath("$.detail").value("Bad credentials"));
	}

	@Test
	public void shouldSignUpUser() throws Exception {
		RegisterUserRequest dto = createRegisterUserRequest("user2@gmail.com", "12345678");
		mockMvc.perform(post("/auth/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		assert(userRepository.findByEmail("user2@gmail.com").isPresent());
	}

	@Test
	public void shouldAuthenticateExistingUser() throws Exception {
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createUserLoginRequest("testuser@gmail.com", "password")))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.token").exists())
				.andExpect(jsonPath("$.expiresIn").exists());
	}

	@Test
	public void shouldReceiveRefreshedToken() throws Exception {
		RegisterUserRequest dto = createRegisterUserRequest("user1@gmail.com", "12345678");
		String loginResponseString = mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createUserLoginRequest("testuser@gmail.com", "password")))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn()
				.getResponse()
				.getContentAsString();
		LoginUserResponse loginUserResponse = objectMapper.readValue(loginResponseString, LoginUserResponse.class);
		mockMvc.perform(post("/auth/refresh")
				.header("Authorization", "Bearer " + loginUserResponse.getToken()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.token").exists());
	}

	@Test
	public void shouldNotReceiveRefreshedToken() throws Exception {
		String loginResponseString = mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createUserLoginRequest("testuser@gmail.com", "password")))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn()
				.getResponse()
				.getContentAsString();
		LoginUserResponse loginUserResponse = objectMapper.readValue(loginResponseString, LoginUserResponse.class);
		String invalidToken = loginUserResponse.getToken().substring(loginUserResponse.getToken().length() - 5) + "XXXXX";
		mockMvc.perform(post("/auth/refresh")
						.header("Authorization", "Bearer " + invalidToken))
				.andExpect(status().isUnauthorized())
				.andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(jsonPath("$.detail").value("Invalid JWT Token"));
	}

	private LoginUserRequest createUserLoginRequest(String email, String password) {
		LoginUserRequest dto = new LoginUserRequest();
		dto.setEmail(email);
		dto.setPassword(password);
		return dto;
	}

	private RegisterUserRequest createRegisterUserRequest (String email, String password) {
		RegisterUserRequest registerUserRequest = new RegisterUserRequest();
		registerUserRequest.setEmail(email);
		registerUserRequest.setPassword(password);
		registerUserRequest.setFullName("Test User");
		return registerUserRequest;
	}
}
