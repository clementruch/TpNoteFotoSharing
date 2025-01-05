package local.epul4a.tpnotefotosharing.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // L'utilisateur qui ajoute un contact

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private User contact; // Le contact ajouté

    @Enumerated(EnumType.STRING)
    private ContactStatus status;

    public enum ContactStatus {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Date d'ajout
}