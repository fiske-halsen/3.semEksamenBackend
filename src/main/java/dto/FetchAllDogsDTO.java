
package dto;


public class FetchAllDogsDTO {
    
     private Object[] dogs;

    public FetchAllDogsDTO(Object[] results) {
        this.dogs = results;
    }

    public Object[] getResults() {
        return dogs;
    }

    public void setResults(Object[] results) {
        this.dogs = results;
    }
}
