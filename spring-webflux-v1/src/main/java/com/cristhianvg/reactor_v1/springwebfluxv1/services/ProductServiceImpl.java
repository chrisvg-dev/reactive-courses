package com.cristhianvg.reactor_v1.springwebfluxv1.services;

import com.cristhianvg.reactor_v1.springwebfluxv1.models.dao.ICategoryDao;
import com.cristhianvg.reactor_v1.springwebfluxv1.models.dao.IProductDao;
import com.cristhianvg.reactor_v1.springwebfluxv1.models.documents.Category;
import com.cristhianvg.reactor_v1.springwebfluxv1.models.documents.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements IProductService {
    private final IProductDao productDao;
    private final ICategoryDao categoryDao;
    @Override
    public Flux<Product> findAll() {
        return this.productDao.findAll();
    }

    @Override
    public Mono<Product> findById(String id) {
        return this.productDao.findById(id);
    }

    @Override
    public Mono<Product> save(Product product) {
        return this.productDao.save(product);
    }

    @Override
    public Mono<Void> delete(Product product) {
        return this.productDao.delete(product);
    }

    @Override
    public Flux<Product> findAllUpperCase() {
        return this.productDao.findAll().map(product -> {
            product.setName(product.getName().toUpperCase());
            return product;
        });
    }

    @Override
    public Mono<Product> findByName(String name) {
        return this.productDao.findByName(name);
    }

    @Override
    public Flux<Category> findAllCategory() {
        return this.categoryDao.findAll();
    }

    @Override
    public Mono<Category> findCategoryById(String id) {
        return this.categoryDao.findById(id);
    }

    @Override
    public Mono<Category> saveCategory(Category category) {
        return this.categoryDao.save(category);
    }
}
