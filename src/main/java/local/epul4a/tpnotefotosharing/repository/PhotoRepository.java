package local.epul4a.tpnotefotosharing.repository;

import local.epul4a.tpnotefotosharing.model.Photo;

public interface PhotoRepository {
    Photo findById(long id);
}
