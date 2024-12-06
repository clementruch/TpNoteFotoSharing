package local.epul4a.tpnotefotosharing.model;

import jakarta.persistence.*;

public class Photo {
    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    public void setOwner(User.Role owner) {
        this.owner=owner;
    }

    public enum Visibility{Private,Public};
    private Visibility visibility;
    private String description;
    private String url;
    @ManyToOne
    @JoinColumn(name = "owner_id")  // La relation ManyToOne avec User, associée à la colonne 'owner_id' dans la base de données
    private User.Role owner;

    // Constructors
    public Photo() {}

    public Photo(String title,Visibility visibility, String description, String url) {
        this.title = title;
        this.visibility = visibility;
        this.description = description;
        this.url=url;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId(){return id;}

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle(){return title;}

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getUrl(){return url;}
    public void setUrl(String url){this.url=url;}
}
