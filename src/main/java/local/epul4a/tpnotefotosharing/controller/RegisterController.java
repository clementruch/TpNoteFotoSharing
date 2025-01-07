package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.model.User;
import local.epul4a.tpnotefotosharing.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private final UserService utilisateurService;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserService utilisateurService, PasswordEncoder passwordEncoder) {
        this.utilisateurService = utilisateurService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password) {
        try {
            logger.info("Registering user: {}", username);
            String encodedPassword = passwordEncoder.encode(password);
            User utilisateur = new User();
            utilisateur.setUsername(username);
            utilisateur.setEmail(email);
            utilisateur.setPassword(encodedPassword);
            utilisateur.setRole(User.Role.USER);
            utilisateurService.saveUser(utilisateur);
            logger.info("User registered successfully: {}", username);
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Error during registration for user {}: {}", username, e.getMessage());
            return "error";
        }
    }
}
