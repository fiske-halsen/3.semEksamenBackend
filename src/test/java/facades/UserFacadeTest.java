package facades;

import dto.CreateNewDogDTO;
import entities.Breed;
import entities.Dog;
import utils.EMF_Creator;

import entities.Role;
import entities.Searches;
import entities.User;
import errorhandling.UserNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;


//@Disabled
public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;
    private static User u1, u2, admin;
    private static Role userRole, adminRole;
    private static Dog d1, d2;
    private static Searches s1, s2;
    private static Breed b1, b2;
    
    public UserFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = UserFacade.getUserFacade(emf);
       EntityManager em = emf.createEntityManager();
    
    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords
    try{
        
        
        
        
        
     u1 = new User("Phillip", "Masterrx8");
     u2 = new User("Sebbergo", "highiq");
     admin = new User("admin", "admin");
     userRole = new Role("user");
     adminRole = new Role("admin");
     d1 = new Dog("Bob", "05/12/2020", "Meget energi", "Husky");
     d2 = new Dog("Hans", "12/08/2017", "Langsom", "Fransk bulldog");
     s1 = new Searches();
     s2 = new Searches();
     b1 = new Breed("Husky", "Meget energi");
     b2 = new Breed ("Fransk bulldog", "langsom");
    
    
    em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM BREED_SEARCHES").executeUpdate();
            em.createNativeQuery("DELETE FROM SEARCHES").executeUpdate();
            em.createNativeQuery("DELETE FROM BREED").executeUpdate();
            em.createNativeQuery("DELETE FROM DOG").executeUpdate();
            em.createNativeQuery("DELETE FROM user_roles").executeUpdate();
            em.createNativeQuery("DELETE FROM roles").executeUpdate();
            em.createNativeQuery("DELETE FROM users").executeUpdate();
    u1.addRole(userRole);
    u2.addRole(userRole);
    admin.addRole(adminRole);
    u1.addDog(d1);
    u1.addDog(d2);
    s1.addBreed(b1);
    s2.addBreed(b2);
    em.persist(userRole);
    em.persist(adminRole);
    em.persist(u1);
    em.persist(u2);
    em.persist(admin);
    em.persist(s1);
    em.persist(s2);
    em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
      
    }
    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

     
    @Test
    public void testVerifyUser() throws AuthenticationException {
        User user = facade.getVeryfiedUser("Phillip", "Masterrx8");
        assertEquals("Phillip", u1.getUserName(), "Expected username is 'Phillip'");
    }
    
     @Test
    public void testAddDogToUser() throws UserNotFoundException{
    CreateNewDogDTO dogDTO = facade.addDogToAUser(new CreateNewDogDTO(u1.getUserName(), "Garfield", "05/03/2015", "Doven", "Saint berner" ));
    String expDogName = "Garfield";
    assertEquals(expDogName, dogDTO.name);
    }
}
