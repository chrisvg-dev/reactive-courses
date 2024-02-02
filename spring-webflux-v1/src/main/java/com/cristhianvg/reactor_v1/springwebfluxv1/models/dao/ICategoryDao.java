package com.cristhianvg.reactor_v1.springwebfluxv1.models.dao;

import com.cristhianvg.reactor_v1.springwebfluxv1.models.documents.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ICategoryDao extends ReactiveMongoRepository<Category, String>{
}
