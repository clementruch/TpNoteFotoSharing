package local.epul4a.tpnotefotosharing.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    public enum Visibility{Private,Public};
    private Visibility visibility;
    private String description;
    private String url;

    // Constructors
    public Photo() {}


    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String url;

    public enum Visibility {
        PRIVATE, PUBLIC
    }
}
