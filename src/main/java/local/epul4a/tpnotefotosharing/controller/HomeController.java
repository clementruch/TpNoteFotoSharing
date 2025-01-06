package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.model.User;
import local.epul4a.tpnotefotosharing.repository.UserRepository;
import local.epul4a.tpnotefotosharing.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import local.epul4a.tpnotefotosharing.service.AlbumService;
import local.epul4a.tpnotefotosharing.model.Album;
import local.epul4a.tpnotefotosharing.service.AlbumService;

import java.util.List;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private PhotoService photoService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("username", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Album> albums = albumService.getAlbumsByOwnerUsername(username); // Récupérer les albums de l'utilisateur
        model.addAttribute("albums", albums);

        // Récupère la liste des photos partagées
        List<Photo> photos = photoService.getPhotosForUser(user.getId());
        model.addAttribute("photos", photos);

        logger.info("Current logged-in user: {}", username);
        return "home";
    }
    @Autowired
    private AlbumService albumService;

}
