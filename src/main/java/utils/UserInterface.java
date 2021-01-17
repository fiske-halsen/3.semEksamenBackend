package utils;

import entities.User;
import dto.CreateNewDogDTO;
import dto.DogsDTO;
import dto.UsersDTO;
import errorhandling.UserNotFoundException;
import security.errorhandling.AuthenticationException;


public interface UserInterface {
    
     public abstract User getVeryfiedUser(String username, String password) throws AuthenticationException ;
     public abstract CreateNewDogDTO addDogToAUser(CreateNewDogDTO dogDTO) throws UserNotFoundException;
     public DogsDTO getAllDogsForUser(String username) throws UserNotFoundException;
    
    
}
