package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.model.Permission;
import local.epul4a.tpnotefotosharing.service.ContactService;
import local.epul4a.tpnotefotosharing.service.PermissionService;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepository;  // Repository pour gérer les photos

    @Autowired
    private PhotoService photoService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private PermissionService permissionService;

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
        Long userId = getCurrentUserId();
        model.addAttribute("photo", new Photo());
        model.addAttribute("userId", userId);
        model.addAttribute("contacts", contactService.getContactsForUser(userId));
        return "addPhoto";
    }


    // Méthode pour ajouter une nouvelle photo après soumission du formulaire
    @PostMapping("/photos/add")
    public String addPhoto(@ModelAttribute Photo photo,
                           @RequestParam("file") MultipartFile file,
                           @RequestParam("visibility") String visibility,
                           @RequestParam(value = "contacts", required = false) List<Long> contactIds) throws IOException {
        Long userId = getCurrentUserId();
        Photo savedPhoto = photoService.uploadPhoto(file, photo.getTitle(), photo.getDescription(), userId, visibility);

        if ("PRIVATE".equals(visibility) && contactIds != null) {
            permissionService.grantPermissionsForContacts(savedPhoto.getId(), contactIds, Permission.PermissionLevel.VIEW);
        }
        return "redirect:/photo/Photo";
    }

    @GetMapping("/photo/Photo")
    public String viewPhotos(Model model, Authentication authentication) {
        Long userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Si l'utilisateur est ADMIN, récupérer toutes les photos
        if (user.getRole() == User.Role.ADMIN) {
            model.addAttribute("photos", photoRepository.findAll());
        } else {
            model.addAttribute("photos", photoService.getPhotosForUser(userId));
        }
        return "Photo";
    }

    @GetMapping("/{id}")
    public String getPhotoDetails(@PathVariable Long id, Model model) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        model.addAttribute("photo", photo);
        return "PhotoDetails";
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
