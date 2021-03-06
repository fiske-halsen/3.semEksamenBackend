package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.CreateNewDogDTO;
import dto.DogDTO;
import dto.DogsDTO;
import dto.SearchDTO;
import entities.User;
import errorhandling.DogNotFoundException;
import errorhandling.UserNotFoundException;
import facades.UserFacade;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;
import utils.SetupTestUsers;

@Path("info")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final ExecutorService ES = Executors.newCachedThreadPool();
    private static final UserFacade FACADE = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static String cachedResponse;
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @Path("cached")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getCached() throws InterruptedException, ExecutionException, TimeoutException {
        return cachedResponse;
    }

    @Path("setupusers")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public void setUpUsers() {
        SetupTestUsers.setUpUsers();
    }

    @Path("alldogs")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllDogs() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        String allDogs = fetcher.AllDogsFetcher.FetchAllDogsExternal(ES, GSON);
        cachedResponse = allDogs;
        return allDogs;
    }

    @Path("dog/{breed}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getDogByBreed(@PathParam("breed") String breed) throws InterruptedException, ExecutionException, TimeoutException, IOException {
        String dog = fetcher.DogFetcher.fetchDogByBreed(ES, GSON, breed);
        cachedResponse = dog;
        return dog;
    }

    @Path("adddog")
    @RolesAllowed("user")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String addDogToUser(String dogDTO) throws UserNotFoundException {
        CreateNewDogDTO dog = GSON.fromJson(dogDTO, CreateNewDogDTO.class);
        CreateNewDogDTO createdDog = FACADE.addDogToAUser(dog);
        return GSON.toJson(createdDog);
    }

    @Path("alldogs/{username}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllDogsByUser(@PathParam("username") String userName) throws UserNotFoundException {
        DogsDTO dogs = FACADE.getAllDogsForUser(userName);
        return GSON.toJson(dogs);
    }

    @Path("delete/{id}")
    @RolesAllowed("user")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String deleteDog(@PathParam("id") long id) throws DogNotFoundException {
        DogDTO deletedDog = FACADE.deleteDog(id);
        return GSON.toJson(deletedDog);
    }

    @Path("edit")
    @RolesAllowed("user")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String editDog(String dogDTO) throws DogNotFoundException {
        DogDTO dog = GSON.fromJson(dogDTO, DogDTO.class);
        DogDTO editedDog = FACADE.editDog(dog);
        return GSON.toJson(editedDog);
    }

    @Path("totalsearches")
    @RolesAllowed("admin")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllSearches() {
        long count = FACADE.getTheTotalNumberOfRequests();
        return "{\"count\":" + count + "}";
    }

    @Path("totalsearches/{breed}")
    @RolesAllowed("admin")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllSearchesByBreed(@PathParam("breed") String breed) {
        long count = FACADE.getTheTotalNumberOfRequestsForBreed(breed);
        return "{\"count\":" + count + "}";
    }

    @Path("addsearch")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String addSearch(String searchDTO) throws UserNotFoundException {
        SearchDTO search = GSON.fromJson(searchDTO, SearchDTO.class);
        SearchDTO searchAdded = FACADE.addSearch(search);
        return GSON.toJson(searchAdded);
    }

}
