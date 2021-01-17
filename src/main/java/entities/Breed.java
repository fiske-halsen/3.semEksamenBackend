package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Breed implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String breedName;
    private String info;

    @ManyToMany
    private List<Searches> searches;

    public Breed() {
    }

    public Breed(String breedName, String info) {
        this.breedName = breedName;
        this.info = info;
        this.searches = new ArrayList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<Searches> getSearches() {
        return searches;
    }

    public void addSearch(Searches search) {
        if (search != null) {
            this.searches.add(search);
            search.getBreeds().add(this);
        }
    }

}
