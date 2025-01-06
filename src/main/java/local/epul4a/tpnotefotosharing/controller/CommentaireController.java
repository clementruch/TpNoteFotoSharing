package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.model.Commentaire;
import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.model.User;
import local.epul4a.tpnotefotosharing.service.CommentaireService;
import local.epul4a.tpnotefotosharing.service.PhotoService;
import local.epul4a.tpnotefotosharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/photo")
public class CommentaireController {

    @Autowired
    private CommentaireService commentaireService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserService userService;

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id, @RequestParam String commentText) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Add the comment
        commentaireService.addComment(id, user.getId(), commentText);

        // Redirect back to the photo detail page
        return "redirect:/photo/" + id + "/details";
    }

    @GetMapping("/{id}/details")
    public String viewPhotoDetails(@PathVariable Long id, Model model) {
        Photo photo = photoService.getPhotoById(id);
        model.addAttribute("photo", photo);
        model.addAttribute("comments", commentaireService.getCommentsByPhotoId(id));
        return "PhotoDetails";
    }
}
