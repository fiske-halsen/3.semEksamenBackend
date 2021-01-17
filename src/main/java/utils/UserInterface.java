package utils;

import entities.User;
import dto.CreateNewDogDTO;
import errorhandling.UserNotFoundException;
import security.errorhandling.AuthenticationException;


public interface UserInterface {
    
     public abstract User getVeryfiedUser(String username, String password) throws AuthenticationException ;
     public abstract CreateNewDogDTO addDogToAUser(CreateNewDogDTO dogDTO) throws UserNotFoundException;
    
    
    
}
