package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.service.PhotoService;
import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.repository.PhotoRepository;
import local.epul4a.tpnotefotosharing.repository.UserRepository;
import local.epul4a.tpnotefotosharing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepository;  // Repository pour gérer les photos

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserRepository userRepository;  // Repository pour gérer les utilisateurs

    // Méthode pour afficher toutes les photos
    @GetMapping("/Photo")
    public String index(Model model) {
        model.addAttribute("photos", photoService.getPhotosForUser(getCurrentUserId()));  // Utilisation de l'ID de l'utilisateur connecté
        return "Photo";  // Nom de la vue Thymeleaf pour afficher les photos
    }

    // Méthode pour afficher le formulaire d'ajout de photo
    @GetMapping("/photos/add")
    public String showAddPhotoForm(Model model) {
        Long userId = getCurrentUserId();  // Récupère l'ID de l'utilisateur connecté
        model.addAttribute("photo", new Photo());  // Créer un nouvel objet Photo et l'ajouter au modèle
        model.addAttribute("userId", userId);  // Ajoute l'ID de l'utilisateur au modèle
        return "addPhoto";  // Nom de la vue pour ajouter une photo
    }

    // Méthode pour ajouter une nouvelle photo après soumission du formulaire
    @PostMapping("/photos/add")
    public String addPhoto(@ModelAttribute Photo photo, @RequestParam("file") MultipartFile file, @RequestParam("visibility") String visibility) throws IOException {
        Long userId = getCurrentUserId();  // Récupérer l'ID de l'utilisateur connecté
        photoService.uploadPhoto(file, photo.getTitle(), photo.getDescription(), userId, visibility);  // Utilisation de l'ID de l'utilisateur connecté
        return "redirect:/photo/Photo";  // Rediriger vers la page d'affichage des photos
    }

    // Récupère l'ID de l'utilisateur actuellement connecté
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
