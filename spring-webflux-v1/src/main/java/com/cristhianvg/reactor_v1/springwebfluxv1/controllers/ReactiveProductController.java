package com.cristhianvg.reactor_v1.springwebfluxv1.controllers;

import com.cristhianvg.reactor_v1.springwebfluxv1.SpringWebfluxV1Application;
import com.cristhianvg.reactor_v1.springwebfluxv1.models.dao.IProductDao;
import com.cristhianvg.reactor_v1.springwebfluxv1.models.documents.Product;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;


@Controller
@RequiredArgsConstructor
public class ReactiveProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ReactiveProductController.class);

    private final IProductDao productDao;

    @GetMapping({"/listAll", "/"})
    public String listAll(Model model) {
        Flux<Product> products = productDao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        });

        products.subscribe(product -> LOG.info(product.getName()));

        model.addAttribute("products", products);
        model.addAttribute("title", "List of Products");
        return "listAll";
    }

    @GetMapping({"/list-data-drive"})
    public String listDataDrive(Model model) {
        /*
        Valida los datos con delay, y va mostrando por bloques los datos que han sido recibidos
         */
        Flux<Product> products = productDao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        }).delayElements(Duration.ofSeconds(1));

        products.subscribe(product -> LOG.info(product.getName()));

        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 2));
        model.addAttribute("title", "List of Products");
        return "listAll";
    }

    @GetMapping({"/list-data-chunked"})
    public String listDataChunked(Model model) {
        /*
        Valida los datos con delay, y va mostrando por bloques los datos que han sido recibidos
         */
        Flux<Product> products = productDao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        }).repeat(5000);

        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 2));
        model.addAttribute("title", "List of Products");
        return "list-chunked";
    }

    @GetMapping({"/list-full"})
    public String listFull(Model model) {
        /*
        Valida los datos con delay, y va mostrando por bloques los datos que han sido recibidos
         */
        Flux<Product> products = productDao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        }).repeat(5000);

        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 2));
        model.addAttribute("title", "List of Products");
        return "list-chunked";
    }
}
