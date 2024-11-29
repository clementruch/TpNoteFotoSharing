package local.epul4a.tpnotefotosharing.service;

import local.epul4a.tpnotefotosharing.model.Permission;
import local.epul4a.tpnotefotosharing.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> getPermissionsForUser(Utilisateur utilisateur) {
        return permissionRepository.findByUtilisateur(utilisateur);
    }

    public void sharePhotoWithUser(Photo photo, Utilisateur utilisateur, Permission.PermissionLevel level) {
        Permission permission = new Permission();
        permission.setPhoto(photo);
        permission.setUtilisateur(utilisateur);
        permission.setPermissionLevel(level);
        permissionRepository.save(permission);
    }

    public boolean canUserViewPhoto(Utilisateur utilisateur, Photo photo) {
        return permissionRepository.findByPhoto(photo).stream()
                .anyMatch(p -> p.getUtilisateur().equals(utilisateur) &&
                        p.getPermissionLevel() == Permission.PermissionLevel.VIEW);
    }
}
