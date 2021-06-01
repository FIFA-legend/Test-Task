package by.bsuir.controllers;

import by.bsuir.entity.Role;
import by.bsuir.entity.User;
import by.bsuir.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User user() {
        return new User();
    }

    @GetMapping("/registration")
    public String registrationForm() {
        return "user_registration";
    }

    @PostMapping("/registration/save")
    public String registerUser(@Valid User user, Errors errors) {
        if (errors.hasErrors() || userService.getUserByUsername(user.getUsername()) != null) {
            return "user_registration";
        }
        user.setRole(Role.USER);
        userService.save(user);
        return "redirect:/login";
    }

}