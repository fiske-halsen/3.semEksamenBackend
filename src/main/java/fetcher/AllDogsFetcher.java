package fetcher;

import com.google.gson.Gson;
import dto.DogFactCombinedDTO;
import dto.FetchAllDogsDTO;
import dto.FetchDogDTO;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import utils.HttpUtils;


public class AllDogsFetcher {
    private static String STARWARS_URL = "https://dog-info.cooljavascript.dk/api/breed";

    public static String FetchAllDogsExternal(ExecutorService threadPool, Gson gson) throws InterruptedException, ExecutionException, TimeoutException, IOException {

        Callable<FetchAllDogsDTO> planetsTask = new Callable<FetchAllDogsDTO>() {
            @Override
            public FetchAllDogsDTO call() throws Exception {
                String planets = HttpUtils.fetchData(STARWARS_URL);
                FetchAllDogsDTO fetchAllDogsDTO = gson.fromJson(planets, FetchAllDogsDTO.class);

                return fetchAllDogsDTO;
            }
        };
        
        Future<FetchAllDogsDTO> futureDogs = threadPool.submit(planetsTask);

        FetchAllDogsDTO dogs = futureDogs.get(2, TimeUnit.SECONDS);

        String dogsToJson = gson.toJson(dogs);
        
        return dogsToJson;
    }
}