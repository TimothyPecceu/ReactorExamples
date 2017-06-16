import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

//A Flux contains 0 to n elements
//A Mono contains at most 1 element
public class ReactorCoreExamples {
    private static List<String> crew = Arrays.asList("Malcolm Reynolds","Zoe Washburne","Hoban Washburne","Kaylee Frye","Jayne Cobb","Inara Serra","River Tam","Simon Tam","Derrial Book");
    private MockedReactiveService service = new MockedReactiveService();

    // Creating a flux from a hardcoded group of values using the just() method.
    // The subscribe() method binds a consumer that will consume the whole sequence
    @Test
    public void fluxFromJust(){
        Flux<String> criminalsFlux = Flux.just("Badger", "Adelei Niska", "Saffron");

        criminalsFlux.subscribe(System.out::println);
    }

    // Creating a flux from the given Iterable using the fromIterable() method.
    @Test
    public void fluxFromIterable(){
        Flux<String> crewFlux = Flux.fromIterable(crew);

        crewFlux.subscribe(System.out::println);
    }

    // Transformations can be applied to flux just like they can be applied to regular java streams
    @Test
    public void transformationsOnFlux(){
        Flux<String> firstNameFlux = service.getFlux()
                .map(p -> p.split(" ")[0])
                .sort();

        firstNameFlux.subscribe(System.out::println);
    }

    // The zipWith() method allows you to merge 2 flux on a step and merge basis.
    @Test
    public void zipWith(){
        Flux<String> firstNameFlux = service.getFlux()
                .map(p -> p.split(" ")[0])
                .sort();

        Flux<String> fullNameFlux = service.getFlux()
                .map(p -> p.split(" ")[1])
                .sort()
                .zipWith(firstNameFlux,(lastName, firstName) -> lastName + " " + firstName);

        fullNameFlux.subscribe(System.out::println);
    }

    // Elements from a flux will be processed as they become available
    @Test
    public void fluxWithDelay() throws InterruptedException{
        Flux<String> crewFlux = service.getDelayedFlux();

       crewFlux.subscribe(System.out::println);
       Thread.sleep(2700);
    }

    // A flux can be set to emit only a limited amount of elements from the sequence
    @Test
    public void fluxWithLimitedEmitting(){
        Flux<String> crewFlux = service.getFlux().take(3);

        crewFlux.subscribe(System.out::println);
    }

    // A flux can be created from multiple Mono being concatenated
    @Test
    public void fluxFromMono(){
        Flux<String> name = Mono.just("Malcolm").concatWith(Mono.just("Reynolds"));

        name.subscribe(System.out::println);
    }

    //When a delay occurs the subscriber will await the next element even when the Flux is built from multiple Mono
    @Test
    public void fluxFromMonoWithDelay() throws InterruptedException{
        Flux<String> name = Mono.just("Malcolm").concatWith(Mono.just("Reynolds").delaySubscription(Duration.ofMillis(500)));

        name.subscribe(System.out::println);
        Thread.sleep(600);
    }

    //Using the firstEmitting() method the first Publisher to start emitting will be used
    @Test
    public void firstEmitting() throws InterruptedException{
        Flux<String> criminalsFlux = Flux.just("Badger", "Adelei Niska", "Saffron").delaySubscription(Duration.ofMillis(500));
        Flux<String> crewFlux = service.getDelayedFlux().take(5);

        Flux.firstEmitting(criminalsFlux,crewFlux)
                .subscribe(System.out::println);
        Thread.sleep(1800);
    }

    //Using the onError methods we can continue the processing using a fallback Flux
    @Test
    public void error() throws InterruptedException{
        Flux<Integer> criminalsFlux = Flux.just("Badger", "Adelei Niska", "Saffron").cast(Integer.class);
        Flux<Integer> numbers = service.getNumberFlux().take(5);

       criminalsFlux.onErrorResume(throwable -> numbers)
                .subscribe(System.out::println);
    }


}
