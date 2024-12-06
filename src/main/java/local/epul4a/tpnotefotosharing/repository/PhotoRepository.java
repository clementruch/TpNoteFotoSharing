package local.epul4a.tpnotefotosharing.repository;

import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<User, Long> {
    Photo findById(long id);

    Photo save(Photo photo);

    List<Photo> findByOwnerId(Long ownerId);

    List<User> findAll();
}
