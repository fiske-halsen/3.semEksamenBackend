package facades;

import dto.CreateNewDogDTO;
import entities.Dog;
import entities.User;
import errorhandling.UserNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

    public static void main(String[] args) throws UserNotFoundException {
        EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
        UserFacade userFacade = UserFacade.getUserFacade(EMF);
        EntityManager em = emf.createEntityManager();

       
        
        
        userFacade.addDogToAUser(new CreateNewDogDTO("Phillip", "Ole", "07/05/2010", "Ekstrem intelligent", "Puddel"));

    }

}
