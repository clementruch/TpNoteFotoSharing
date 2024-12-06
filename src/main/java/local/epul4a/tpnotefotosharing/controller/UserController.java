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

    /**
     * Display the registration form.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Handle user registration.
     */
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

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        //WE ADD A ERROR MESSAGE IN THE SOUT to indicate the user that it tryed to login
        logger.info("User tryed to login 45545454545");

        if (error != null) {
            model.addAttribute("message", "Invalid username or password!");
            logger.warn("Login failed: Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
            logger.info("User logged out successfully.");
        }




        return "login";
    }
}
