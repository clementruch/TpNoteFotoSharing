package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepository;  // Repository pour gérer les photos

    // Méthode pour afficher toutes les photos
    @GetMapping("/Photo")
    public String index(Model model) {
        model.addAttribute("photos", photoRepository.findAll());  // Ajouter la liste des photos au modèle
        return "Photo";  // Nom de la vue Thymeleaf pour afficher les photos
    }

    // Méthode pour afficher le formulaire d'ajout de photo
    @GetMapping("/photos/add")
    public String showAddPhotoForm(Model model) {
        model.addAttribute("photo", new Photo());  // Créer un nouvel objet Photo et l'ajouter au modèle
        return "addPhoto";  // Nom de la vue pour ajouter une photo
    }

    // Méthode pour ajouter une nouvelle photo après soumission du formulaire
    @PostMapping("/photos/add")
    public String addPhoto(@ModelAttribute Photo photo) {
        // Enregistrer la photo dans la base de données via le repository
        photoRepository.save(photo);
        return "redirect:/photo/Photo";  // Rediriger vers la page d'affichage des photos
    }
}
