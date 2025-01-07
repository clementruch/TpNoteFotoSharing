package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.model.Contact;
import local.epul4a.tpnotefotosharing.model.Permission;
import local.epul4a.tpnotefotosharing.service.ContactService;
import local.epul4a.tpnotefotosharing.service.PermissionService;
import local.epul4a.tpnotefotosharing.service.PhotoService;
import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.repository.PhotoRepository;
import local.epul4a.tpnotefotosharing.repository.UserRepository;
import local.epul4a.tpnotefotosharing.model.User;
import local.epul4a.tpnotefotosharing.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import local.epul4a.tpnotefotosharing.model.Album;
import local.epul4a.tpnotefotosharing.service.AlbumService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.Optional;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumService albumService;
    // Méthode pour afficher toutes les photos
    @GetMapping("/Photo")
    public String index(Model model) {
        Long userId = getCurrentUserId();
        List<Photo> photos = photoService.getPhotosForUser(userId);

        List<Map<String, Object>> photosWithPermissions = photos.stream().map(photo -> {
            Map<String, Object> photoData = new HashMap<>();
            photoData.put("photo", photo);

            boolean canModify = isAdminOrCanModifyPhoto(userId, photo);
            photoData.put("canModify", canModify);

            return photoData;
        }).collect(Collectors.toList());

        model.addAttribute("photosWithPermissions", photosWithPermissions);
        return "Photo";
    }

    private boolean isAdminOrCanModifyPhoto(Long userId, Photo photo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRole() == User.Role.ADMIN || photo.getOwner().getId().equals(userId);
    }

    @GetMapping("/photos/add")
    public String showAddPhotoForm(Model model) {
        Long userId = getCurrentUserId();
        model.addAttribute("photo", new Photo());
        model.addAttribute("userId", userId);
        model.addAttribute("contacts", contactService.getContactsForUser(userId));
        return "addPhoto";
    }

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
        System.out.println("DEBUG - Current User: " + authentication.getName());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == User.Role.ADMIN) {
            model.addAttribute("photos", photoRepository.findAll());
        } else {
            model.addAttribute("photos", photoService.getPhotosForUser(userId));
        }
        return "Photo";
    }

    @GetMapping("/{id}")
    public String getPhotoDetails(@PathVariable Long id, Model model, Authentication authentication) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean canModify = user.getRole() == User.Role.ADMIN || photo.getOwner().getId().equals(user.getId());
        model.addAttribute("canModify", canModify);
        model.addAttribute("photo", photo);
        return "PhotoDetails";
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
  
    @GetMapping("/{photoId}/add-to-album")
    public String showAddToAlbumForm(@PathVariable Long photoId, Model model) {
        Long userId = getCurrentUserId();
        Photo photo = photoService.getPhotoById(photoId);
        List<Album> albums = albumService.getAlbumsByOwnerId(userId);

        model.addAttribute("photo", photo);
        model.addAttribute("albums", albums);
        return "addToAlbum";
    }

    @PostMapping("/{photoId}/add-to-album")
    public String addPhotoToAlbum(@PathVariable Long photoId, @RequestParam Long albumId) {
        photoService.addPhotoToAlbum(photoId, albumId);
        return "redirect:/photo/Photo";
    }

    @PostMapping("/delete/{photoId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or @photoService.isOwner(authentication.name, #photoId)")
    public String deletePhoto(@PathVariable Long photoId) {
        photoService.deletePhoto(photoId);
        return "redirect:/photo/Photo";
    }

    @GetMapping("/update/{photoId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or @photoService.isOwner(authentication.name, #photoId)")
    public String showUpdateForm(@PathVariable Long photoId, Model model) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        Long userId = getCurrentUserId();
        List<Contact> contacts = contactService.getContactsForUser(userId);
        List<User> sharedWith = permissionService.getUsersWithAccessToPhoto(photoId);

        model.addAttribute("photo", photo);
        model.addAttribute("contacts", contacts);
        model.addAttribute("sharedWith", sharedWith);
        return "updatePhotoForm";
    }

    @PostMapping("/update/{photoId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or @photoService.isOwner(authentication.name, #photoId)")
    public String updatePhoto(@PathVariable Long photoId,
                              @RequestParam String title,
                              @RequestParam String description,
                              @RequestParam String visibility,
                              @RequestParam(value = "contacts", required = false) List<Long> contactIds) {
        photoService.updatePhoto(photoId, title, description, visibility);

        if ("PRIVATE".equals(visibility)) {
            permissionService.updatePermissionsForPhoto(photoId, contactIds);
        } else {
            permissionService.removePermissionsForPhoto(photoId);
        }

        return "redirect:/photo/Photo";
    }
}
