package dto;


public class FetchDogDTO {
 
    public String breed;
    public String info;
    public String wikipedia;

    public FetchDogDTO(String breed, String info, String wikipedia) {
        this.breed = breed;
        this.info = info;
        this.wikipedia = wikipedia;
    }
}
