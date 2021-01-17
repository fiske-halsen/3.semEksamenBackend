package facades;

import dto.CreateNewDogDTO;
import dto.DogDTO;
import dto.DogsDTO;
import dto.SearchDTO;
import dto.UsersDTO;
import entities.Breed;
import entities.Dog;
import entities.Searches;
import entities.User;
import errorhandling.DogNotFoundException;
import errorhandling.UserNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;
import utils.UserInterface;

public class UserFacade implements UserInterface {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    @Override
    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    @Override
    public CreateNewDogDTO addDogToAUser(CreateNewDogDTO dogDTO) throws UserNotFoundException {
        EntityManager em = emf.createEntityManager();
        User user;
        Dog dog;

        try {
            user = em.find(User.class, dogDTO.userName);

            if (user == null) {
                throw new UserNotFoundException("Username does not exist");
            }
            dog = new Dog(dogDTO.name, dogDTO.dateOfBirth, dogDTO.info, dogDTO.breed);

            user.addDog(dog);
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
        return new CreateNewDogDTO(dog);
    }

    @Override
    public DogsDTO getAllDogsForUser(String username) throws UserNotFoundException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null) {
                throw new UserNotFoundException("Username does not exist");
            }
            Query query = em.createQuery("SELECT d FROM Dog d JOIN d.user u WHERE u.userName =:username", Dog.class);
            query.setParameter("username", username);
            List<Dog> dogs = query.getResultList();
            return new DogsDTO(dogs);
        } finally {
            em.close();
        }

    }
    
    @Override
    public DogDTO deleteDog(long dogId) throws DogNotFoundException {
        EntityManager em = emf.createEntityManager();
        Dog dog;
        try {
           dog = em.find(Dog.class, dogId);
            if (dog == null) {
                throw new DogNotFoundException("Username does not exist");
            }
            em.getTransaction().begin();
            em.remove(dog);
            em.getTransaction().commit();
            return new DogDTO(dog);
        } finally {
            em.close();
        }
    }

    @Override
    public DogDTO editDog(DogDTO dogDTO) throws DogNotFoundException {
        EntityManager em = emf.createEntityManager();
        Dog dog;
        try {
           dog = em.find(Dog.class, dogDTO.id);
            if (dog == null) {
                throw new DogNotFoundException("Username does not exist");
            }
            
            dog.setName(dogDTO.name);
            dog.setDateOfBirth(dogDTO.dateOfBirth);
            dog.setInfo(dogDTO.info);
            dog.setBreed(dogDTO.breed);
            
            em.getTransaction().begin();
            em.merge(dog);
            em.getTransaction().commit();
            return new DogDTO(dog);
        } finally {
            em.close();
        }
    }
    
    @Override
    public long getTheTotalNumberOfRequests() {
        EntityManager em = emf.createEntityManager();
        long count = 0;
        try {
            Query query = em.createQuery("SELECT COUNT(s) FROM Searches s");
            count = (long) query.getSingleResult();
             return count;
        } finally {
            em.close();
        }
    }

    @Override
    public long getTheTotalNumberOfRequestsForBreed(String breed) {
         EntityManager em = emf.createEntityManager();
        long count = 0;
        try {
            Query query = em.createQuery("SELECT COUNT(s) FROM Searches s JOIN s.breeds b WHERE b.breedName=:breed ");
            query.setParameter("breed", breed);
            count = (long) query.getSingleResult();
             return count;
        } finally {
            em.close();
        }
    }
    
    
    @Override
    public SearchDTO addSearch(SearchDTO searchDTO) {
       EntityManager em = emf.createEntityManager();
       Searches search;
       Breed breed;
        try {
           
            search = new Searches();
            
            breed = em.find(Breed.class, searchDTO.name);
            
            if(breed == null){
             breed = new Breed(searchDTO.name, searchDTO.info);
            }
            
            search.addBreed(breed);
            
            em.getTransaction().begin();
            
            em.persist(search);
            
            em.getTransaction().commit();
            
            System.out.println();
            
             return new SearchDTO(breed.getBreedName(), breed.getInfo());
        } finally {
            em.close();
        }
    }
    
    
    public static void main(String[] args) throws UserNotFoundException, DogNotFoundException {
        EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
        UserFacade userFacade = UserFacade.getUserFacade(EMF);
        EntityManager em = emf.createEntityManager();

        
        
        
        
       
       
    }

}
