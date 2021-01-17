package utils;

import entities.User;
import dto.CreateNewDogDTO;
import dto.DogDTO;
import dto.DogsDTO;
import dto.UsersDTO;
import errorhandling.DogNotFoundException;
import errorhandling.UserNotFoundException;
import security.errorhandling.AuthenticationException;


public interface UserInterface {
    
     public abstract User getVeryfiedUser(String username, String password) throws AuthenticationException ;
     public abstract CreateNewDogDTO addDogToAUser(CreateNewDogDTO dogDTO) throws UserNotFoundException;
     public DogsDTO getAllDogsForUser(String username) throws UserNotFoundException;
     public DogDTO deleteDog(long dogId) throws DogNotFoundException;
     public DogDTO editDog(DogDTO dogDTO) throws DogNotFoundException;
    
}
