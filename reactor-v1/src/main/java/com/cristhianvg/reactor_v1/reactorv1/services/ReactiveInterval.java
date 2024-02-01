package com.cristhianvg.reactor_v1.reactorv1.services;

import com.cristhianvg.reactor_v1.reactorv1.ReactorV1Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class ReactiveInterval {
    private static final Logger LOG = LoggerFactory.getLogger(ReactiveInterval.class);


    public static void infiniteInterval() throws InterruptedException {
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(1));
        CountDownLatch latch = new CountDownLatch(1);
        delay
            .doOnTerminate(latch::countDown)
            .flatMap(s -> {
                if (s > 5) {
                    return Flux.error(new InterruptedException("Only 5 seconds allowed."));
                }
                return Flux.just(s);
            })
            .map(s -> "Hello " + s.toString())
            .retry(3)
            //.blockLast(); // Subscribe and block until process end
            .subscribe(s -> LOG.info(s.toString()), error -> LOG.error(error.getMessage())); // Non-blocking and spending resources

        latch.await(); // Block the main thread
    }

    public static void interval() {
        Flux<Integer> range = Flux.range(1, 12);
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(1));

        range.zipWith(delay, (ra, de) -> ra)
                .doOnNext(s -> System.out.println(s.toString()))
                .blockLast(); // Subscribe and block until process ends
                // .subscribe(); // Non-blocking and spending resources
    }

    public static void delayElement() throws InterruptedException {
        Flux<Integer> range = Flux.range(1, 12)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(s -> System.out.println(s.toString()));
        //range.subscribe(); // Non-blocking and spending resources
        range.blockLast(); // Subscribe and block until process ends NOT RECOMMENDED
        //Thread.sleep(13000);
    }
}
