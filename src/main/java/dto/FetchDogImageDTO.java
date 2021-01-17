package dto;


public class FetchDogImageDTO {
    
    
 public String breed;
 public String image;

    public FetchDogImageDTO(String breed, String image) {
        this.breed = breed;
        this.image = image;
    }
}
