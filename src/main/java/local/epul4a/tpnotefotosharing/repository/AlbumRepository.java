package local.epul4a.tpnotefotosharing.repository;

import local.epul4a.tpnotefotosharing.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByOwnerId(Long ownerId);
    List<Album> findByVisibility(Album.Visibility visibility);
    @Query("SELECT a FROM Album a WHERE a.owner.username = :username")
    List<Album> findByOwnerUsername(@Param("username") String username);
}
