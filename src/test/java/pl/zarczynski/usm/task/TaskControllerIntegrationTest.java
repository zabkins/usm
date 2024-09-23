package pl.zarczynski.usm.task;

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
import pl.zarczynski.usm.common.DateHelper;
import pl.zarczynski.usm.task.dto.CreateTaskDto;
import pl.zarczynski.usm.task.dto.UpdateTaskDto;
import pl.zarczynski.usm.task.entity.TaskStatus;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldPerformAllRequestsSuccessfully() throws Exception {
		//login
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
		//create two tasks
		mockMvc.perform(post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(getCreateTaskDto()))
						.header("Authorization", "Bearer " + loginUserResponse.getToken())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1));
		mockMvc.perform(post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(getCreateTaskDto()))
						.header("Authorization", "Bearer " + loginUserResponse.getToken())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(2));
		//find
		mockMvc.perform(get("/tasks/1")
						.header("Authorization", "Bearer " + loginUserResponse.getToken())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1));
		//find all
		mockMvc.perform(get("/tasks")
						.header("Authorization", "Bearer " + loginUserResponse.getToken())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content").value(hasSize(2)));
		//update task
		mockMvc.perform(put("/tasks/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(getUpdateTaskDto()))
						.header("Authorization", "Bearer " + loginUserResponse.getToken())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name").value("task - updated"));
		//delete
		mockMvc.perform(delete("/tasks/2")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(getCreateTaskDto()))
						.header("Authorization", "Bearer " + loginUserResponse.getToken())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(204));
		//find all
		mockMvc.perform(get("/tasks")
						.header("Authorization", "Bearer " + loginUserResponse.getToken())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content").value(hasSize(1)));
	}

	private LoginUserRequest createUserLoginRequest(String email, String password) {
		LoginUserRequest dto = new LoginUserRequest();
		dto.setEmail(email);
		dto.setPassword(password);
		return dto;
	}

	private CreateTaskDto getCreateTaskDto() {
		CreateTaskDto dto = new CreateTaskDto();
		dto.setName("task");
		dto.setDescription("description");
		ZonedDateTime now = ZonedDateTime.now();
		String start = DateHelper.parseDate(now);
		String finish = DateHelper.parseDate(now.plusWeeks(1));
		String startDate = start.substring(0, start.lastIndexOf(" "));
		String finishDate = finish.substring(0, finish.lastIndexOf(" "));
		dto.setStartDate(startDate);
		dto.setFinishDate(finishDate);
		return dto;
	}

	private UpdateTaskDto getUpdateTaskDto() {
		UpdateTaskDto dto = new UpdateTaskDto();
		dto.setName("task - updated");
		dto.setDescription("description - updated");
		ZonedDateTime now = ZonedDateTime.now();
		String start = DateHelper.parseDate(now);
		String finish = DateHelper.parseDate(now.plusWeeks(1));
		String startDate = start.substring(0, start.lastIndexOf(" "));
		String finishDate = finish.substring(0, finish.lastIndexOf(" "));
		dto.setStartDate(startDate);
		dto.setFinishDate(finishDate);
		dto.setStatus(TaskStatus.IN_PROGRESS);
		return dto;
	}
}
