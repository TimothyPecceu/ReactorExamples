import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MockedReactiveService {
    private static List<String> crew = Arrays.asList("Malcolm Reynolds","Zoe Washburne","Hoban Washburne","Kaylee Frye","Jayne Cobb","Inara Serra","River Tam","Simon Tam","Derrial Book");

    private Random random = new Random();

    public Flux<String> getFlux(){
        return Flux.fromIterable(crew);
    }

    public Flux<String> getDelayedFlux(){
        return Flux.fromIterable(crew).delayElements(Duration.ofMillis(300));
    }

    public Flux<Integer> getNumberFlux(){
        return Flux.just(1,2,3,4,5,6,7,8,9,10);
    }
}
