package com.cristhianvg.reactor_v1.reactorv1.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class ReactiveRange {
    private static final Logger LOG = LoggerFactory.getLogger(ReactiveRange.class);

    public static void zipWithRange() {
        Flux.just(1, 2, 3, 4)
                .map(i -> i * 2)
                .zipWith(Flux.range(0, 4), (one, two) -> String.format("First Flux: %d, Second Flux: %d", one, two))
                .subscribe(s -> LOG.info(s));
    }
}
