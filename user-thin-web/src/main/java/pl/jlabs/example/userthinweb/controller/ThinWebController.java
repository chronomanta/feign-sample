package pl.jlabs.example.userthinweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jlabs.example.feign.client.UsersAPI;
import pl.jlabs.example.feign.client.model.User;
import pl.jlabs.example.feign.client.model.UserData;

/**
 * MVC Controller for Thymeleaf - transparently calls user-server using {@link org.springframework.cloud.openfeign.FeignClient}
 */
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ThinWebController {

    /**
     * Autowired HTTP client created by Feign
     */
    private final UsersAPI usersAPI;

    @GetMapping
    public String root() {
        return "redirect:/list";
    }

    @GetMapping("list")
    public String listUsers(Model model) {
        model.addAttribute("users", usersAPI.getUsers());
        return "listUsers";
    }

    @GetMapping("new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserData("", ""));
        return "addUser";
    }

    @PostMapping("add")
    public String addUser(@ModelAttribute UserData userData) {
        usersAPI.createUser(userData);
        return "redirect:/list";
    }

    @GetMapping("edit/{userId}")
    public String editUser(@PathVariable("userId") Long userId, Model model) {
        final User user = usersAPI.getUser(userId);
        model.addAttribute("user", user.userData());
        model.addAttribute("userId", user.userId());
        return "editUser";
    }

    @PostMapping("update/{userId}")
    public String updateUser(@PathVariable("userId") Long userId, @ModelAttribute UserData userData) {
        usersAPI.updateUser(userId, userData);
        return "redirect:/list";
    }

    @GetMapping("delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        usersAPI.deleteUser(userId);
        return "redirect:/list";
    }

}
