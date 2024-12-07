package local.epul4a.tpnotefotosharing.service;

import local.epul4a.tpnotefotosharing.model.Permission;
import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.model.User;
import local.epul4a.tpnotefotosharing.repository.PermissionRepository;
import local.epul4a.tpnotefotosharing.repository.PhotoRepository;
import local.epul4a.tpnotefotosharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private UserRepository userRepository;

    public Permission grantPermission(Long photoId, Long userId, Permission.PermissionLevel permissionLevel) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Permission permission = new Permission();
        permission.setPhoto(photo);
        permission.setUser(user);
        permission.setPermissionLevel(permissionLevel);

        return permissionRepository.save(permission);
    }

    public Permission getPermission(Long photoId, Long userId) {
        return permissionRepository.findByPhotoIdAndUserId(photoId, userId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    public List<Permission> getPermissionsForPhoto(Long photoId) {
        return permissionRepository.findByPhotoId(photoId);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public void deletePermission(Long photoId, Long userId) {
        Permission permission = permissionRepository.findByPhotoIdAndUserId(photoId, userId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        permissionRepository.delete(permission);
    }
}
