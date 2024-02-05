package pl.jlabs.example.feign.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.jlabs.example.feign.client.UsersAPI;
import pl.jlabs.example.feign.client.model.User;
import pl.jlabs.example.feign.client.model.UserData;
import pl.jlabs.example.feign.server.service.UsersService;

import java.util.List;
import java.util.Optional;

/**
 * It implements {@link pl.jlabs.example.feign.client.UsersAPI} from client, which is a FeignClient interface - thanks to it, we keep both client and controller uniform
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UsersAPIController implements UsersAPI {

    private final UsersService usersService;

    @Override
    public List<User> getUsers() {
        return usersService.getUsers();
    }

    @Override
    public Long createUser(UserData user) {
        return usersService.createUser(user);
    }

    @Override
    public User getUser(Long userId) {
        final Optional<User> user = Optional.ofNullable(usersService.getUser(userId));
        return user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public void updateUser(Long userId, UserData user) {
        final Optional<User> updated = Optional.ofNullable(usersService.updateUser(userId, user));
        updated.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public void deleteUser(Long userId) {
        usersService.deleteUser(userId);
    }


}
