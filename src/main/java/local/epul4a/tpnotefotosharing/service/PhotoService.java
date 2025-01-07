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

    // Méthode pour récupérer une photo par ID
    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with id: " + id));
    }

    public List<Photo> getPhotosForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == User.Role.ADMIN) {
            // Si l'utilisateur est un admin, retourner toutes les photos
            return photoRepository.findAll();
        }

        List<Photo> allPhotos = photoRepository.findAll();
        List<Photo> visiblePhotos = new ArrayList<>();

        for (Photo photo : allPhotos) {
            if (photo.getOwner() == null || photo.getUrl() == null) {
                continue; // Ignorer les photos avec des informations manquantes
            }
            if (photo.getOwner().getId().equals(userId)) {
                visiblePhotos.add(photo); // Ajouter les photos de l'utilisateur lui-même
            } else if (photo.getVisibility() == Photo.Visibility.PUBLIC) {
                visiblePhotos.add(photo); // Ajouter les photos publiques

            } else {
                Permission permission = permissionRepository.findByPhotoIdAndUserId(photo.getId(), userId).orElse(null);
                if (permission != null) {
                    visiblePhotos.add(photo); // Ajouter les photos accessibles par permissions
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

        String filePath = saveFile(file);

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Photo photo = new Photo();
        photo.setTitle(title);
        photo.setDescription(description);
        photo.setUrl(filePath);
        photo.setVisibility(Photo.Visibility.valueOf(visibility));
        photo.setOwner(owner);

        return photoRepository.save(photo);
    }

    private boolean isValidFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }

    private String saveFile(MultipartFile file) throws IOException {
        Path path = Paths.get(UPLOAD_DIR);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
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


    public boolean isOwner(String username, Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        boolean isOwner = photo.getOwner().getUsername().equals(username);

        System.out.println("DEBUG - isOwner: username=" + username +
                ", photoId=" + photoId +
                ", owner=" + photo.getOwner().getUsername() +
                ", result=" + isOwner);

        return isOwner;
    }

    public void deletePhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        photoRepository.delete(photo);
    }

    public void updatePhoto(Long photoId, String title, String description, String visibility) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        photo.setTitle(title);
        photo.setDescription(description);
        photo.setVisibility(Photo.Visibility.valueOf(visibility));
        photoRepository.save(photo);
    }
}
