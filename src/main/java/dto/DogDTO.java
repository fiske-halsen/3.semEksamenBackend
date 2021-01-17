package dto;

import entities.Dog;
import java.util.Objects;

public class DogDTO {
    public long id;
    public String name;
    public String dateOfBirth;
    public String info;
    public String breed;

    public DogDTO(Dog dog) {
        this.id = dog.getId();
        this.name = dog.getName();
        this.dateOfBirth = dog.getDateOfBirth();
        this.info = dog.getInfo();
        this.breed = dog.getBreed();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + Objects.hashCode(this.dateOfBirth);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DogDTO other = (DogDTO) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.dateOfBirth, other.dateOfBirth)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
