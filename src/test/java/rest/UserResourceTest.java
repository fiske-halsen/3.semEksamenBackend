package rest;

import dto.CreateNewDogDTO;
import dto.DogDTO;
import dto.SearchDTO;
import entities.Breed;
import entities.Dog;
import entities.Role;
import entities.Searches;
import entities.User;
import facades.UserFacade;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test

//@Disabled
public class UserResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static UserFacade facade;
    private static User u1, u2, admin;
    private static Role userRole, adminRole;
    private static Dog d1, d2;
    private static Searches s1, s2;
    private static Breed b1, b2;
    private static String securityToken;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

        facade = UserFacade.getUserFacade(emf);
        EntityManager em = emf.createEntityManager();

        // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
        // CHANGE the three passwords below, before you uncomment and execute the code below
        // Also, either delete this file, when users are created or rename and add to .gitignore
        // Whatever you do DO NOT COMMIT and PUSH with the real passwords
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
        // CHANGE the three passwords below, before you uncomment and execute the code below
        // Also, either delete this file, when users are created or rename and add to .gitignore
        // Whatever you do DO NOT COMMIT and PUSH with the real passwords
        try {

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
            b2 = new Breed("Fransk bulldog", "langsom");

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
            u2.addDog(d2);
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
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
//    @BeforeEach
//    public void setUp() {
//        EntityManager em = emf.createEntityManager();
//        r1 = new RenameMe("Some txt", "More text");
//        r2 = new RenameMe("aaa", "bbb");
//        try {
//            em.getTransaction().begin();
//            em.createNamedQuery("RenameMe.deleteAllRows").executeUpdate();
//            em.persist(r1);
//            em.persist(r2);
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
//    }
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");

        //System.out.println("TOKEN ---> " + securityToken);
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/info").then().statusCode(200);
    }

    //This test assumes the database contains two rows
    @Test
    public void testDummyMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/info/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello anonymous"));
    }

    @Test
    public void testAddADogToAUser() throws Exception {
        login("Phillip", "Masterrx8");

        given()
                .contentType("application/json")
                .body(new CreateNewDogDTO("Phillip", "Ole", "05/02/2020", "Meget sulten", "Golden"))
                .header("x-access-token", securityToken)
                .when()
                .post("/info/adddog").then()
                .statusCode(HttpStatus.OK_200.getStatusCode());

    }

    @Test
    public void testGetAllDogsByUser() throws Exception {
        List<DogDTO> dogsDTO;

        dogsDTO = given()
                .contentType("application/json")
                .when()
                .get("/info/alldogs/" + u1.getUserName())
                .then()
                .extract().body().jsonPath().getList("dogs", DogDTO.class);

        DogDTO dogDTO = new DogDTO(d1);

        assertThat(dogsDTO, containsInAnyOrder(dogDTO));
    }

    @Test
    public void testDeleteDog() throws Exception {
        login("Phillip", "Masterrx8");

        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .delete("/info/delete/" + d1.getId()).then()
                .statusCode(HttpStatus.OK_200.getStatusCode());

    }

    @Test
    public void testEditDog() throws Exception {
        login("Phillip", "Masterrx8");
        
        d1.setName("EditedName");
        
        given()
                .contentType("application/json")
                .body(new DogDTO(d1))
                .header("x-access-token", securityToken)
                .when()
                .put("/info/edit").then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo("EditedName"));

    }

    
    @Test
    public void testGetAllSearches() throws Exception {
        login("admin", "admin");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/info/totalsearches/").then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }
    
    @Test
    public void testGetAllSearchesByBreed() throws Exception {
        login("admin", "admin");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/info/totalsearches/" + b1.getBreedName()).then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(1));
    }
    
   
    @Test
    public void testAddASearch() throws Exception {
        given()
                .contentType("application/json")
                .body(new SearchDTO("DummyName", "DummyInfo"))
                .when()
                .post("/info/addsearch/").then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo("DummyName"));

    }
    
    
    
    
}
