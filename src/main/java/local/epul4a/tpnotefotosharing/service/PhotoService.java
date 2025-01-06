package local.epul4a.tpnotefotosharing.service;

import local.epul4a.tpnotefotosharing.model.Permission;
import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.model.User;
import local.epul4a.tpnotefotosharing.repository.PermissionRepository;
import local.epul4a.tpnotefotosharing.repository.PhotoRepository;
import local.epul4a.tpnotefotosharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import local.epul4a.tpnotefotosharing.model.Album;
import local.epul4a.tpnotefotosharing.repository.AlbumRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumService albumService;


    private static final String UPLOAD_DIR = "uploads/";
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    // Méthode pour récupérer les photos visibles par un utilisateur
    public List<Photo> getPhotosForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Récupérer toutes les photos si l'utilisateur est un ADMIN
        if (user.getRole() == User.Role.ADMIN) {
            return photoRepository.findAll();
        }

        // Récupérer toutes les photos visibles pour un utilisateur non-admin
        List<Photo> allPhotos = photoRepository.findAll();
        List<Photo> visiblePhotos = new ArrayList<>();

        for (Photo photo : allPhotos) {
            if (photo.getOwner().getId().equals(userId)) {
                // Si l'utilisateur est le propriétaire de la photo
                visiblePhotos.add(photo);
            } else if (photo.getVisibility() == Photo.Visibility.PUBLIC) {
                // Si la photo est publique
                visiblePhotos.add(photo);
            } else {
                // Vérifiez si l'utilisateur a une permission sur cette photo
                Permission permission = permissionRepository.findByPhotoIdAndUserId(photo.getId(), userId).orElse(null);
                if (permission != null) {
                    visiblePhotos.add(photo);
                }
            }
        }
        return visiblePhotos;
    }


    public Photo uploadPhoto(MultipartFile file, String title, String description, Long ownerId, String visibility) throws IOException {
        if (!isValidFileType(file)) {
            throw new RuntimeException("Invalid file format. Only JPEG and PNG are allowed.");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new RuntimeException("File size exceeds the maximum limit of 5 MB.");
        }

        // Sauvegarder le fichier sur le serveur
        String filePath = saveFile(file);

        // Récupérer l'utilisateur (propriétaire de la photo) à partir de son ID
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Créer un objet Photo et l'enregistrer dans la base de données
        Photo photo = new Photo();
        photo.setTitle(title);
        photo.setDescription(description);
        photo.setUrl(filePath);
        photo.setVisibility(Photo.Visibility.valueOf(visibility));
        photo.setOwner(owner);

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

        return filePath.toString().replace("uploads" + File.separator, "");
    }
    public void addPhotoToAlbum(Long photoId, Long albumId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found with ID: " + photoId));
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found with ID: " + albumId));

        photo.setAlbum(album); // Associe la photo à l'album
        photoRepository.save(photo); // Enregistre les modifications
    }
    public Photo getPhotoById(Long photoId) {
        return photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found with ID: " + photoId));
    }


}
