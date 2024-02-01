package com.cristhianvg.reactor_v1.springwebfluxv1.controllers;

import com.cristhianvg.reactor_v1.springwebfluxv1.models.dao.IProductDao;
import com.cristhianvg.reactor_v1.springwebfluxv1.models.documents.Product;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class ReactiveProductRestController {
    private static final Logger LOG = LoggerFactory.getLogger(ReactiveProductController.class);
    private final IProductDao productDao;

    @GetMapping
    public Flux<Product> index() {
        return productDao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        })
        // .repeat(2000) // Works
        .doOnNext(prod -> LOG.info(prod.getName()));
    }

    @GetMapping("/{id}")
    public Mono<Product> show(@PathVariable("id") String id) {
        //return productDao.findById(id).map(product -> {
        //product.setName(product.getName().toUpperCase());
        //    return product;
        //}).doOnNext(prod -> LOG.info(prod.getName()));
        Flux<Product> products =  productDao.findAll();
        Mono<Product> product = products.filter(p -> p.getId().equals(id))
                .next() // Return Mono from Flux
                .doOnNext(prod -> LOG.info(prod.getName()));
        return product;
    }
}
