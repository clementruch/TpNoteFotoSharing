package local.epul4a.tpnotefotosharing.service;

import local.epul4a.tpnotefotosharing.model.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class SecurityService {

    @Autowired
    private PhotoService photoService;

    public boolean canModifyPhoto(Long userId, Photo photo) {
        return photo.getOwner().getId().equals(userId) ||
                SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR"));
    }
}
