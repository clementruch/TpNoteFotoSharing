package local.epul4a.tpnotefotosharing.repository;

import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<User, Long> {
    Photo findById(long id);

    Photo save(Photo photo);

}
