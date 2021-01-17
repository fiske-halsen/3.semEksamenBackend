package utils;


import entities.Breed;
import entities.Dog;
import entities.Role;
import entities.Searches;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {

  public static void setUpUsers() {

    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();
    
    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords

    User u1 = new User("Phillip", "Masterrx8");
    User u2 = new User("Sebbergo", "highiq");
    User admin = new User("admin", "admin");
    Role userRole = new Role("user");
    Role adminRole = new Role("admin");
    Dog d1 = new Dog("Bob", "05/12/2020", "Meget energi", "Husky");
    Dog d2 = new Dog("Hans", "12/08/2017", "Langsom", "Fransk bulldog");
    Searches s1 = new Searches();
    Searches s2 = new Searches();
    Breed b1 = new Breed("Husky", "Meget energi");
    Breed b2 = new Breed ("Fransk bulldog", "langsom");
    
    
    
    em.getTransaction().begin();
    
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
    System.out.println("PW: " + u1.getUserPass());
    System.out.println("Testing user with OK password: " + u1.verifyPassword("Masterrx8"));
    System.out.println("Testing user with wrong password: " + u1.verifyPassword("test1"));
    System.out.println("Created TEST Users");
   
  }

}
