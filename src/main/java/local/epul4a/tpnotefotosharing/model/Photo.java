package local.epul4a.tpnotefotosharing.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public Photo(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Commentaire> comments;

    // Enum for visibility
    public enum Visibility {
        PRIVATE, PUBLIC
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;


}
