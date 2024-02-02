package com.cristhianvg.reactor_v1.springwebfluxv1.models.dao;

import com.cristhianvg.reactor_v1.springwebfluxv1.models.documents.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IProductDao extends ReactiveMongoRepository<Product, String>{
    Mono<Product> findByName(String name);
}
