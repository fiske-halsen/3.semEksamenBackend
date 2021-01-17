package dto;

import entities.Dog;
import entities.User;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    public String userName;
    public List<DogDTO> dogs = new ArrayList();

    public UserDTO(User user) {
        this.userName = user.getUserName();

        for (Dog dog : user.getDogs()) {
            this.dogs.add(new DogDTO(dog));

        }

    }

}
