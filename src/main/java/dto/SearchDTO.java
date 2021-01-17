
package dto;

import entities.Searches;
import java.util.Date;

public class SearchDTO {
    
    public String name;
    public String info;
    public Date dateTime;

    public SearchDTO(Searches search) {
        this.name = name;
        this.info = info;
    }
       public SearchDTO(String name, String info) {
        this.name = name;
        this.info = info;
    }
}
