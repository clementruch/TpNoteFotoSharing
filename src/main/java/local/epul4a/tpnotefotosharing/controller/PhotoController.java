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


    @GetMapping("/photos")
    public String index(Model model) {
        // Add photos to the model
        model.addAttribute("photos", photoRepository.findAll());
        return "photos"; // Return the name of the Thymeleaf template (e.g., photos.html)
    }

    @PostMapping("/addPhoto")
    public String addPhoto(String title, String url, String description) {
        Photo photo = new Photo();
        photo.setTitle(title);
        photo.setUrl(url); // Ensure to set the URL
        photo.setDescription(description);
        photoRepository.save(photo); // Save the photo to the database
        return "redirect:/photos"; // Redirect back to the photos page

    }
}
