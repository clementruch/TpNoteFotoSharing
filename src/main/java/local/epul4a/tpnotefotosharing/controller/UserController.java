package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.model.User;
import local.epul4a.tpnotefotosharing.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            userService.saveUser(user);
            model.addAttribute("message", "User registered successfully!");
            logger.info("User registered successfully: {}", user.getUsername());
        } catch (RuntimeException e) {
            model.addAttribute("message", "User registration failed: " + e.getMessage());
            logger.error("User registration failed for {}: {}", user.getUsername(), e.getMessage(), e);
        }
        return "register";
    }
}
