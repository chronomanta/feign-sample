package pl.jlabs.example.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pl.jlabs.example.feign.client.model.User;
import pl.jlabs.example.feign.client.model.UserData;

import java.util.List;

/**
 * This interface will be automatically implemented by Feign
 */
@FeignClient(name = "user-server", url = "${user-server.url:http://localhost:8080}")
public interface UsersAPI {

    @GetMapping
    List<User> getUsers();

    @PostMapping
    Long createUser(@RequestBody UserData user);

    @GetMapping("/{userId}")
    User getUser(@PathVariable("userId") Long userId);

    @PutMapping("/{userId}")
    void updateUser(@PathVariable("userId") Long userId, @RequestBody UserData user);

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable("userId") Long userId);

}
