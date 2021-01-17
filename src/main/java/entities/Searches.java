package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Searches implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date searchDate;
    
    
    @ManyToMany(mappedBy="searches", cascade=CascadeType.PERSIST)
    private List<Breed> breeds;

   

    public Searches() {
        this.searchDate = new Date();
        this.breeds = new ArrayList();
    }

    public List<Breed> getBreeds() {
        return breeds;
    }

    public void addBreed(Breed breed) {
        System.out.println(breed.getBreedName() + "---------------------------------------");
        if(breed != null){
           this.breeds.add(breed);
           
            System.out.println("Crasher den?");
           breed.getSearches().add(this);
        }
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }
    
    
    
    
    

}
