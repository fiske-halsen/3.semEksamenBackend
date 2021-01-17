package dto;

import entities.Dog;


public class CreateNewDogDTO {
    public String userName;
    public String name;
    public String dateOfBirth;
    public String info;
    public String breed;

    public CreateNewDogDTO(Dog dog) {
        this.userName = dog.getUser().getUserName();
        this.name = dog.getName();
        this.dateOfBirth = dog.getDateOfBirth();
        this.info = dog.getInfo();
        this.breed = dog.getBreed();
    }

    public CreateNewDogDTO(String userName, String name, String dateOfBirth, String info, String breed) {
        this.userName = userName;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.info = info;
        this.breed = breed;
    }
    
    
    
    
}
