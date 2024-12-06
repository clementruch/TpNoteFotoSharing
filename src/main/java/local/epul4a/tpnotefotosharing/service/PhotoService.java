package local.epul4a.tpnotefotosharing.service;

import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.model.User;
import local.epul4a.tpnotefotosharing.repository.PhotoRepository;
import local.epul4a.tpnotefotosharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "/uploads/";
    private static final long MAX_SIZE = 5 * 1024 * 1024;
    public Photo uploadPhoto(MultipartFile file, String title, String description, Long ownerId, String visibility) throws IOException {
        if (!isValidFileType(file)) {
            throw new RuntimeException("Invalid file format. Only JPEG and PNG are allowed.");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new RuntimeException("File size exceeds the maximum limit of 5 MB.");
        }

        // Sauvegarder le fichier sur le serveur
        String filePath = saveFile(file);

        // Récupérer l'utilisateur
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("User not found"));

        // Créer un objet Photo et l'enregistrer dans la base de données
        Photo photo = new Photo();
        photo.setTitle(title);
        photo.setDescription(description);
        photo.setUrl(filePath);
        photo.setVisibility(Photo.Visibility.valueOf(visibility));
        photo.setOwner(owner.getRole());

        return photoRepository.save(photo);
    }

    // Vérification du format de fichier (JPEG, PNG)
    private boolean isValidFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }

    // Sauvegarder le fichier sur le serveur
    private String saveFile(MultipartFile file) throws IOException {
        // Créer un répertoire si nécessaire
        Path path = Paths.get(UPLOAD_DIR);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // Générez un nom unique pour le fichier pour éviter les conflits
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        // Sauvegarder le fichier
        Path filePath = path.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();  // Retourne le chemin du fichier
    }

    public List<Photo> getPhotosByUser(Long userId) {
        return photoRepository.findByOwnerId(userId);
    }
}
