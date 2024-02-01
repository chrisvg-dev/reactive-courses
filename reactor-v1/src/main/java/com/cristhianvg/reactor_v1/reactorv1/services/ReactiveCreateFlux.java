package com.cristhianvg.reactor_v1.reactorv1.services;

import reactor.core.publisher.Flux;

import java.util.Timer;
import java.util.TimerTask;

public class ReactiveCreateFlux {

    public static void intervalFromCreate() {
        Flux.create(emitter -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                private Integer count = 0;
                @Override
                public void run() {
                    emitter.next(++count);

                    if (count == 10) {
                        timer.cancel();
                        emitter.complete();
                    }

                    if (count == 5) {
                        timer.cancel();
                        emitter.error(new InterruptedException("Error, flux interrupted"));
                    }
                }
            }, 1000, 1000);
        })
        .subscribe(
            next -> System.out.println("Next: " + next),
            error -> System.out.println("Error: " + error.getMessage()),
            () -> System.out.println("Complete"));
    }

}
