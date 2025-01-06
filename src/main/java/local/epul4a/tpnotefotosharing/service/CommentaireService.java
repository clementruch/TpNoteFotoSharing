package local.epul4a.tpnotefotosharing.service;

import local.epul4a.tpnotefotosharing.model.Commentaire;
import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.model.User;
import local.epul4a.tpnotefotosharing.repository.CommentaireRepository;
import local.epul4a.tpnotefotosharing.repository.PhotoRepository;
import local.epul4a.tpnotefotosharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentaireService {

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Commentaire> getCommentsByPhotoId(Long photoId) {
        return commentaireRepository.findByPhotoId(photoId);
    }

    public Commentaire addComment(Long photoId, Long userId, String text) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Commentaire commentaire = new Commentaire();
        commentaire.setPhoto(photo);
        commentaire.setAuthor(user);
        commentaire.setText(text);
        return commentaireRepository.save(commentaire);
    }
}
