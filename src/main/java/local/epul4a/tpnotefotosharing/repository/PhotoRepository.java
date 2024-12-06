package local.epul4a.tpnotefotosharing.repository;

import local.epul4a.tpnotefotosharing.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PhotoRepository extends JpaRepository<User, Long> {
    Photo findById(long id);

    Photo save(Photo photo);

}
