package fetcher;

import com.google.gson.Gson;
import dto.DogFactCombinedDTO;
import dto.FetchDogDTO;
import dto.FetchDogFactDTO;
import dto.FetchDogImageDTO;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import utils.HttpUtils;

public class DogFetcher {

    private static final String DOG_INFO_URL = "https://dog-info.cooljavascript.dk/api/breed/";
    private static final String DOG_IMAGE_URL = "https://dog-image.cooljavascript.dk/api/breed/random-image/";
    private static final String DOG_FACT_URL = "https://dog-api.kinduff.com/api/facts";
    

    public static String fetchDogByBreed(ExecutorService threadPool, Gson gson, String breed) throws InterruptedException, ExecutionException, TimeoutException {

        Callable<FetchDogDTO> dogTask = new Callable<FetchDogDTO>() {
            @Override
            public FetchDogDTO call() throws Exception {
                String dog = HttpUtils.fetchData(DOG_INFO_URL + breed);
                FetchDogDTO dogDTO = gson.fromJson(dog, FetchDogDTO.class);

                return dogDTO ;
            }
        };

        Callable<FetchDogImageDTO> dogImageTask = new Callable<FetchDogImageDTO>() {
            @Override
            public FetchDogImageDTO call() throws Exception {
                String dog = HttpUtils.fetchData(DOG_IMAGE_URL + breed);
                FetchDogImageDTO dogImageDTO = gson.fromJson(dog, FetchDogImageDTO.class);

                return dogImageDTO;
            }
        };

        Callable<FetchDogFactDTO> dogFactTask = new Callable<FetchDogFactDTO>() {
            @Override
            public FetchDogFactDTO call() throws Exception {
                String dog = HttpUtils.fetchData(DOG_FACT_URL);
                FetchDogFactDTO fetchDogFactDTO = gson.fromJson(dog, FetchDogFactDTO.class);

                return fetchDogFactDTO;
            }
        };
    
        Future<FetchDogDTO> futureDog = threadPool.submit(dogTask);
        Future<FetchDogImageDTO> futureDogImage = threadPool.submit(dogImageTask);
        Future<FetchDogFactDTO> futureDogFact = threadPool.submit(dogFactTask);
       
        FetchDogDTO dog = futureDog.get(2, TimeUnit.SECONDS);
        FetchDogImageDTO dogImage = futureDogImage.get(2, TimeUnit.SECONDS);
        FetchDogFactDTO dogFacts = futureDogFact.get(2, TimeUnit.SECONDS);
       

        DogFactCombinedDTO combinedDTO = new DogFactCombinedDTO(dog, dogImage, dogFacts);
        String combinedJSON = gson.toJson(combinedDTO);

        return combinedJSON;
    }

}
