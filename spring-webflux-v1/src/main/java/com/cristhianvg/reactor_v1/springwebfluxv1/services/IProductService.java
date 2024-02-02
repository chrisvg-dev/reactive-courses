package com.cristhianvg.reactor_v1.springwebfluxv1.services;

import com.cristhianvg.reactor_v1.springwebfluxv1.models.documents.Category;
import com.cristhianvg.reactor_v1.springwebfluxv1.models.documents.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {
    Flux<Product> findAll();
    Mono<Product> findById(String id);
    Mono<Product> save(Product product);
    Mono<Void> delete(Product product);
    Flux<Product> findAllUpperCase();
    Mono<Product> findByName(String name);

    Flux<Category> findAllCategory();
    Mono<Category> findCategoryById(String id);
    Mono<Category> saveCategory(Category category);
}
