package local.epul4a.tpnotefotosharing.controller;

import local.epul4a.tpnotefotosharing.model.Permission;
import local.epul4a.tpnotefotosharing.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    // Attribuer une permission à un utilisateur sur une photo
    @PostMapping("/grant")
    public String grantPermission(@RequestParam Long photoId,
                                  @RequestParam Long userId,
                                  @RequestParam Permission.PermissionLevel permissionLevel) {
        permissionService.grantPermission(photoId, userId, permissionLevel);
        return "redirect:/photo/Photo";
    }

    // Supprimer une permission d'un utilisateur sur une photo
    @PostMapping("/revoke")
    public String revokePermission(@RequestParam Long photoId,
                                   @RequestParam Long userId) {
        permissionService.deletePermission(photoId, userId);
        return "redirect:/photo/Photo";
    }

    // Afficher les permissions d'une photo
    @GetMapping("/list/{photoId}")
    public String listPermissions(@PathVariable Long photoId, Model model) {
        model.addAttribute("permissions", permissionService.getPermissionsForPhoto(photoId));
        return "permissions";
    }
}
