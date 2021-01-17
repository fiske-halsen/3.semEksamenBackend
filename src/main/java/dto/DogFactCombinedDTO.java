/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author lukas
 */
public class DogFactCombinedDTO {

    public String breed;
    public String info;
    public String wikipedia;
    public String image;
    public String[] facts;
    public boolean success;

    public DogFactCombinedDTO(FetchDogDTO fetchDogDTO, FetchDogImageDTO fetchDogImageDTO, FetchDogFactDTO fetchDogFactDTO ) {
        this.breed = fetchDogDTO.breed;
        this.info = fetchDogDTO.info;
        this.wikipedia = fetchDogDTO.wikipedia;
        this.image = fetchDogImageDTO.image;
        this.facts = fetchDogFactDTO.facts;
        this.success = fetchDogFactDTO.success;
       
    }

}
