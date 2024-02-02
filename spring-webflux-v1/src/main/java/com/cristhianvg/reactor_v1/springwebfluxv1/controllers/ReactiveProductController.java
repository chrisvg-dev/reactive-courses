package com.cristhianvg.reactor_v1.springwebfluxv1.controllers;

import com.cristhianvg.reactor_v1.springwebfluxv1.models.documents.Category;
import com.cristhianvg.reactor_v1.springwebfluxv1.models.documents.Product;
import com.cristhianvg.reactor_v1.springwebfluxv1.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Date;

import java.time.Duration;
@Controller
@SessionAttributes("product")
@RequiredArgsConstructor
public class ReactiveProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ReactiveProductController.class);

    private final IProductService productService;

    @ModelAttribute("categories") // Carga los datos de la lista de categorias
    public Flux<Category> categories() {
        return productService.findAllCategory();
    }

    @GetMapping("/delete/{id}")
    public Mono<String> delete(@PathVariable String id) {
        return productService.findById(id)
                .defaultIfEmpty(new Product())
                .flatMap(p -> {
                    if (p.getId() == null) {
                        return Mono.error(new InterruptedException("Product doesn't exist"));
                    }
                    return Mono.just(p);
                })
                .flatMap(productService::delete)
                .then(Mono.just("redirect:/listAll?success=Product+deleted+successfully"))
                .onErrorResume(ex -> Mono.just("redirect:/listAll?error=Product+doesn't+exist"));
    }
    @GetMapping("/save")
    public Mono<String> save(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("title", "Save Product");
        return Mono.just("save-view");
    }
    @PostMapping("/save")
    public Mono<String> save(@Valid Product product, BindingResult result, SessionStatus status, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Form errors");
            return Mono.just("save-view");
        }
        status.setComplete();

        return productService.findCategoryById(product.getCategory().getId())
            .flatMap(c -> {
                if (product.getCreatedAt() == null) {
                    product.setCreatedAt(new Date());
                }
                product.setCategory(c);
                return productService.save(product);
            })
            .switchIfEmpty(Mono.error(new InterruptedException("Category doesn't exist")))
            .doOnNext(p -> {
                LOG.info("Product saved: " + p);
            }).then(Mono.just("redirect:/listAll"));
    }
    @GetMapping("/save-record/{id}")
    public Mono<String> updateV2(@PathVariable("id") String id, Model model) {
        return productService.findById(id)
                .doOnNext(p -> {
                    LOG.info("Product found: " + p.getName() + ", id: " + p.getId());
                    model.addAttribute("product", p);
                    model.addAttribute("title", "Edit Product");
                })
                .defaultIfEmpty(new Product())
                .flatMap(p -> {
                    if (p.getId() == null) {
                        return Mono.error(new InterruptedException("Product doesn't exist"));
                    }
                    return Mono.just(p);
                })
                .then(Mono.just("save-view"))
                .onErrorResume(ex -> Mono.just("redirect:/listAll?error=Product+doesn't+exist"));
    }

    @GetMapping("/save/{id}")
    public Mono<String> update(@PathVariable String id, Model model) {
        Mono<Product> product = productService.findById(id)
            .doOnNext(p -> LOG.info("Product found: " + p.getName() + ", id: " + p.getId()))
            .defaultIfEmpty(new Product());
        model.addAttribute("product", product);
        model.addAttribute("title", "Edit Product");
        return Mono.just("save-view");
    }

    @GetMapping({"/listAll", "/"})
    public String listAll(Model model) {
        Flux<Product> products = productService.findAll().map(product -> {
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
        Flux<Product> products = productService.findAll().map(product -> {
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
        Flux<Product> products = productService.findAll().map(product -> {
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
        Flux<Product> products = productService.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        }).repeat(5000);

        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 2));
        model.addAttribute("title", "List of Products");
        return "list-chunked";
    }
}
