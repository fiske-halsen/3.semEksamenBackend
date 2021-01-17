
package dto;


public class FetchDogFactDTO {
    
    
   public String[] facts;
   public boolean success;

    public FetchDogFactDTO(String[] facts, boolean success) {
        this.facts = facts;
        this.success = success;
    }
    
}
