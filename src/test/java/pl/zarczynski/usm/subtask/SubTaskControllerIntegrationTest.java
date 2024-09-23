package pl.zarczynski.usm.subtask;

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
import pl.zarczynski.usm.subtask.dto.CreateSubTaskDto;
import pl.zarczynski.usm.subtask.dto.UpdateSubTaskDto;
import pl.zarczynski.usm.task.dto.CreateTaskDto;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SubTaskControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldPerformAllActionsSuccessfully () throws Exception {
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
		//create task
		mockMvc.perform(post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(getCreateTaskDto()))
						.header("Authorization", "Bearer " + loginUserResponse.getToken())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		//create SubTask
		mockMvc.perform(post("/tasks/1/subtasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(getCreateSubTaskDto()))
						.accept(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + loginUserResponse.getToken())
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1));
		//create secondSubTask
		mockMvc.perform(post("/tasks/1/subtasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(getCreateSubTaskDto()))
						.accept(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + loginUserResponse.getToken())
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(2));
		//updateSubTask
		mockMvc.perform(put("/tasks/subtasks/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(getUpdateSubTaskDto()))
						.accept(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + loginUserResponse.getToken()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("updated name"))
				.andExpect(jsonPath("$.description").value("updated description"));
		//getAllSubtasks
		mockMvc.perform(get("/tasks/1/subtasks")
						.accept(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + loginUserResponse.getToken()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").value(hasSize(2)));
		//delete SubTask
		mockMvc.perform(delete("/tasks/subtasks/2")
						.header("Authorization", "Bearer " + loginUserResponse.getToken()))
				.andExpect(status().is(204));
		//findAll SubTasks again
		mockMvc.perform(get("/tasks/1/subtasks")
						.accept(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + loginUserResponse.getToken()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").value(hasSize(1)));
	}

	private UpdateSubTaskDto getUpdateSubTaskDto () {
		UpdateSubTaskDto updateSubTaskDto = new UpdateSubTaskDto();
		updateSubTaskDto.setName("updated name");
		updateSubTaskDto.setDescription("updated description");
		updateSubTaskDto.setDone(true);
		return updateSubTaskDto;
	}

	private CreateSubTaskDto getCreateSubTaskDto () {
		CreateSubTaskDto createSubTaskDto = new CreateSubTaskDto();
		createSubTaskDto.setName("name");
		createSubTaskDto.setDescription("description");
		return createSubTaskDto;
	}

	private CreateTaskDto getCreateTaskDto () {
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

	private LoginUserRequest createUserLoginRequest (String email, String password) {
		LoginUserRequest dto = new LoginUserRequest();
		dto.setEmail(email);
		dto.setPassword(password);
		return dto;
	}
}
