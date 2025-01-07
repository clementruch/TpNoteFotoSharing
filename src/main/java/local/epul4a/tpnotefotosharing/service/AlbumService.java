package local.epul4a.tpnotefotosharing.service;

import local.epul4a.tpnotefotosharing.model.Album;
import local.epul4a.tpnotefotosharing.model.Photo;
import local.epul4a.tpnotefotosharing.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import local.epul4a.tpnotefotosharing.repository.UserRepository;
import local.epul4a.tpnotefotosharing.model.User;

import java.util.List;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private UserRepository userRepository;

    public Album createAlbum(String name, String title, String description, Album.Visibility visibility, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + ownerId));

        Album album = new Album();
        album.setName(name);
        album.setTitle(title);
        album.setDescription(description);
        album.setVisibility(visibility);
        album.setOwner(owner);

        return albumRepository.save(album);
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    public Album getAlbumById(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album not found with ID: " + id));
    }

    public List<Album> getAlbumsByOwnerUsername(String username) {
        return albumRepository.findByOwnerUsername(username);
    }
    public List<Album> getAlbumsByOwnerId(Long ownerId) {
        return albumRepository.findByOwnerId(ownerId);
    }
}

