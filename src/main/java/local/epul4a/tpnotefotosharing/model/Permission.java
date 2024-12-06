package local.epul4a.tpnotefotosharing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "partage")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    private PermissionLevel permissionLevel;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum PermissionLevel {
        VIEW, EDIT, ADMIN
    }
}
