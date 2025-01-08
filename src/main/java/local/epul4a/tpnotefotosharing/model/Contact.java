package local.epul4a.tpnotefotosharing.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String username;

    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private User contact;

    @Enumerated(EnumType.STRING)
    private ContactStatus status;

    public enum ContactStatus {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    @Column(nullable = false)
    private boolean selected = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}