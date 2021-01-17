package dto;

import entities.Dog;
import entities.User;
import java.util.ArrayList;
import java.util.List;

public class DogsDTO {

    public List<DogDTO> dogs = new ArrayList();

    public DogsDTO(List<Dog> dogs) {

        dogs.forEach(dog -> {
            this.dogs.add(new DogDTO(dog));
        });
    }

}
