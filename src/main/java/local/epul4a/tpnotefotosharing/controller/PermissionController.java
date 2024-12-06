package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.service.PermissionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/share")
    public String sharePhoto(@RequestParam Long photoId,
                             @RequestParam Long userId,
                             @RequestParam String permissionLevel) {

        // ToDo : recup les photoId et utilisaterusId quand les services seront fait pour faire le partage

        permissionService.sharePhotoWithUser(photo, utilisateur, level);
        return "Photo partagée avec succès..";
    }
}
