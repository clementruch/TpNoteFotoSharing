package local.epul4a.tpnotefotosharing.repository;

import local.epul4a.tpnotefotosharing.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findByUtilisateur(Utilisateur utilisateur);
    List<Permission> findByPhoto(Photo photo);
}