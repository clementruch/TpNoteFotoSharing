package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.model.Utilisateur;
import local.epul4a.tpnotefotosharing.service.UtilisateurService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    private final UtilisateurService utilisateurService;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UtilisateurService utilisateurService, PasswordEncoder passwordEncoder) {
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
        String encodedPassword = passwordEncoder.encode(password);
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(username);
        utilisateur.setEmail(email);
        utilisateur.setPasswordHash(encodedPassword);
        utilisateur.setRole("USER");

        utilisateurService.save(utilisateur);
        return "redirect:/login";
    }
}
