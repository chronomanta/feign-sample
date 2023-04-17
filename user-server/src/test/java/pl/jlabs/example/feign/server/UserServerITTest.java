package pl.jlabs.example.feign.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.jlabs.example.feign.client.model.User;
import pl.jlabs.example.feign.client.model.UserData;
import pl.jlabs.example.feign.server.controller.UsersAPIController;
import pl.jlabs.example.feign.server.service.UsersService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersAPIController.class)
class UserServerITTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@SpyBean
	private UsersService usersService;

	@AfterEach
	void cleanup() {
		usersService.clear();
	}

	@Test
	void shouldServiceCRUDCorrectly() {
		// given
		val user1Data = new UserData("User", "First");
		val user2Data = new UserData("User", "Second");
		val user3Data = new UserData("Another", "First");
		val user2Changed = new UserData("Another", "Second");
		// read all
		assertThat(getUsers()).isEmpty();
		// create
		val user1Id = createUser(user1Data);
		val user2Id = createUser(user2Data);
		val user3Id = createUser(user3Data);
		// read
		assertThat(getUser(user1Id).userData()).isEqualTo(user1Data);
		assertThat(getUser(user2Id).userData()).isEqualTo(user2Data);
		assertThat(getUser(user3Id).userData()).isEqualTo(user3Data);
		// read all
		assertThat(getUsers().stream().map(User::userData).toList()).containsExactlyInAnyOrder(user1Data, user2Data, user3Data);
		// update
		updateUser(user2Id, user2Changed);
		// delete
		deleteUser(user1Id);
		assertThat(getUsers().stream().map(User::userData).toList()).containsExactlyInAnyOrder(user2Changed, user3Data);
	}

	@SneakyThrows
	private List<User> getUsers() {
		val usersJson = mockMvc.perform(get("")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return objectMapper.readValue(usersJson, new TypeReference<List<User>>() {});
	}

	@SneakyThrows
	private Long createUser(UserData userData) {
		val userId = mockMvc.perform(post("").content(objectMapper.writeValueAsString(userData)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return Long.parseLong(userId);
	}

	@SneakyThrows
	private User getUser(Long userId) {
		val userJson = mockMvc.perform(get("/" + userId))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return objectMapper.readValue(userJson, User.class);
	}

	@SneakyThrows
	private void updateUser(Long userId, UserData userData) {
		mockMvc.perform(put("/" + userId).content(objectMapper.writeValueAsString(userData)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
	}

	@SneakyThrows
	private void deleteUser(Long userId) {
		mockMvc.perform(delete("/" + userId))
				.andExpect(status().isOk()).andReturn();
	}

}
