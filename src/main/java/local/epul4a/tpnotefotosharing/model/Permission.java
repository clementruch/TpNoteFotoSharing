package local.epul4a.tpnotefotosharing.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name="partage")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionLevel permissionLevel;

    private LocalDateTime createdAt;

    public enum PermissionLevel {
        VIEW, EDIT, ADMIN
    }
}
