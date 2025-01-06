package local.epul4a.tpnotefotosharing.repository;

import jakarta.transaction.Transactional;
import local.epul4a.tpnotefotosharing.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPhotoIdAndUserId(Long photoId, Long userId);

    // Ajoute une méthode pour trouver toutes les permissions d'une photo
    List<Permission> findByPhotoId(Long photoId);

    // Ajoute une méthode pour supprimer les permissions par photoId
    @Transactional
    @Modifying
    @Query("DELETE FROM Permission p WHERE p.photo.id = :photoId")
    void deleteByPhotoId(@Param("photoId") Long photoId);
}
