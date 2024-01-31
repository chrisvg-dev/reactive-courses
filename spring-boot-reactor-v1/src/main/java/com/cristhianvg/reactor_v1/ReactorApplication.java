package com.cristhianvg.reactor_v1;

import com.cristhianvg.reactor_v1.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ReactorApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(ReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        flatMap();
    }

    public void iterable() {
        // Flux = observable
        // doOnNext is a function that is executed for each element of the flux
        // Los flujos son independientes, no se ejecutan en orden
        // Son inmutables, no se pueden modificar

        List<String> namesList = new ArrayList<>();
        namesList.add("Cristhian");
        namesList.add("Pepe");
        namesList.add("Sofia");
        namesList.add("Camila");
        namesList.add("Mariana");
        namesList.add("Lalo");
        Flux<String> namesFlux = Flux.fromIterable(namesList); // Create a new flux from a list

        // Pure Flux
        Flux<String> names = Flux.just("Cristhian", "Pepe", "Sofia", "Camila", "Mariana", "Lalo"); // Flujos independientes
        //.doOnNext(LOG::info);
        //.doOnNext(e -> {
        //    if (e.isEmpty()) throw new RuntimeException("Name can not be empty");
        //    LOG.info(e);
        //})
        Flux<User> users = names.map(name -> new User(name.toUpperCase(), null)) // Flujos independientes
                .filter(e -> e.getName().equalsIgnoreCase("cristhian"))
                .doOnNext(e -> {
                    if (e.getName().isEmpty()) throw new RuntimeException("Name can not be empty");
                    LOG.info(e.getName());
                });
        // names.subscribe(); v1
        // names.subscribe(LOG::info);
        // names.subscribe(e -> LOG.info(e), error -> LOG.error(error.getMessage())); // Can use 2 anonymous functions

        // Can use 3 anonymous functions ofr each case, can use onComplete
        // If there is an error, the flux cannot be completed
        namesFlux.subscribe(user -> LOG.info(user.toString()), error -> LOG.error(error.getMessage()), () -> LOG.info("Process finished successfully"));
        //names.subscribe(user -> LOG.info(user.toString()), error -> LOG.error(error.getMessage()), () -> LOG.info("Process finished successfully"));
        //users.subscribe(user -> LOG.info(user.toString()), error -> LOG.error(error.getMessage()), () -> LOG.info("Process finished successfully"));
    }

    public void flatMap() {
        // flatMap convierte a un flujo reactivo, retorna Flux o Mono
        List<String> namesList = new ArrayList<>();
        namesList.add("Cristhian Villegas");
        namesList.add("Pepe Lalo");
        namesList.add("Sofia Vergara");
        namesList.add("Camila Mendez");
        namesList.add("Mariana Lauriani");
        namesList.add("Lalo Pepe");
        Flux.fromIterable(namesList)
                .map(name -> new User(name.toUpperCase().split(" ")[0], name.toUpperCase().split(" ")[1])) // Flujos independientes
                .flatMap(user -> {
                    if (user.getName().equalsIgnoreCase("Cristhian")) {
                        return Mono.just(user);
                    }
                    return Mono.empty();
                }).subscribe(user -> LOG.info(user.toString()), error -> LOG.error(error.getMessage()), () -> LOG.info("Process finished successfully"));
    }
}