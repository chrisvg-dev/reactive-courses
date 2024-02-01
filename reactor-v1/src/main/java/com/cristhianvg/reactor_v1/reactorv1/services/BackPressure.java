package com.cristhianvg.reactor_v1.reactorv1.services;

import com.cristhianvg.reactor_v1.reactorv1.ReactorV1Application;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class BackPressure {
    private static final Logger LOG = LoggerFactory.getLogger(BackPressure.class);

    /**
     * Backpressure is a mechanism that allows the subscriber to control how fast or slow the publisher produces data.
     * Could be processed per loop, per second, per minute, per batch, etc.
     */
    public static void backPressureV2() {
        // We can manage the backpressure using the method limitRate
        // We can implement the backpressure using the Subscriber interface and modifying the request method
        Flux.range(1, 10)
            .log() // Shows current thread and process
            .limitRate(5) // Indicate how many elements we want to process
            .subscribe();
    }

    public static void backPressure() {
        // We can manage the backpressure using the method limitRate
        // We can implement the backpressure using the Subscriber interface and modifying the request method
        Flux.range(1, 10)
                .log() // Shows current thread and process
                .subscribe(new Subscriber<Integer>() {

                    private Subscription subscription;
                    private Integer limit = 5;
                    private Integer consumed = 0;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        this.subscription.request(limit); // Indicate how many elements we want to process
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LOG.info("Element: {}", integer);
                        this.consumed++;

                        if (this.consumed == this.limit) {
                            this.consumed = 0;
                            this.subscription.request(this.limit);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {
                        LOG.info("Process finished");
                    }
                });
    }
}
