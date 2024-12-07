package local.epul4a.tpnotefotosharing.repository;

import local.epul4a.tpnotefotosharing.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPhotoIdAndUserId(Long photoId, Long userId);

    // Ajoute une méthode pour trouver toutes les permissions d'une photo
    List<Permission> findByPhotoId(Long photoId);
}
