package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.model.Album;
import local.epul4a.tpnotefotosharing.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import local.epul4a.tpnotefotosharing.model.User;
import local.epul4a.tpnotefotosharing.service.UserService;


@Controller
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listAlbums(Model model) {
        model.addAttribute("albums", albumService.getAllAlbums());
        return "albumList";
    }

    @GetMapping("/{id}")
    public String viewAlbum(@PathVariable Long id, Model model) {
        // Récupérer les détails de l'album
        Album album = albumService.getAlbumById(id);
        if (album == null) {
            throw new RuntimeException("Album not found with ID: " + id);
        }

        // Ajouter l'album et ses photos au modèle
        model.addAttribute("album", album);
        model.addAttribute("photos", album.getPhotos());
        return "albumDetails"; // Retourne le fichier albumDetails.html
    }

    @GetMapping("/add")
    public String showAddAlbumForm() {
        return "addAlbum";
    }

    // Méthode POST pour gérer la soumission du formulaire
    @PostMapping("/add")
    public String addAlbum(@RequestParam String name,
                           @RequestParam String title,
                           @RequestParam(required = false) String description,
                           @RequestParam Album.Visibility visibility,
                           Authentication authentication) {
        String username = authentication.getName(); // Récupération du nom d'utilisateur connecté
        User owner = userService.getUserByUsername(username); // Récupération de l'utilisateur connecté
        albumService.createAlbum(name, title, description, visibility, owner.getId());
        return "redirect:/albums";
    }


}
