package local.epul4a.tpnotefotosharing.repository;

import local.epul4a.tpnotefotosharing.model.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    List<Commentaire> findByPhotoId(Long photoId);
}
