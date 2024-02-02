package com.cristhianvg.reactor_v1.springwebfluxv1;

import com.cristhianvg.reactor_v1.springwebfluxv1.models.documents.Category;
import com.cristhianvg.reactor_v1.springwebfluxv1.models.documents.Product;
import com.cristhianvg.reactor_v1.springwebfluxv1.services.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class SpringWebfluxV1Application implements CommandLineRunner {
	private static final Logger LOG = LoggerFactory.getLogger(SpringWebfluxV1Application.class);
	@Autowired private IProductService productService;
	@Autowired private ReactiveMongoTemplate mongoTemplate;
	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxV1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// productDao.deleteAll().subscribe(resp -> LOG.info("Deleted all products"));
		mongoTemplate.dropCollection("products").subscribe();
		mongoTemplate.dropCollection("categories").subscribe();

		Category electronics = new Category("Electronics");
		Category sports = new Category("Sports");
		Category computers = new Category("Computers");
		Category books = new Category("Books");
		Flux.just(electronics, sports, computers, books)
			.flatMap(category -> productService.saveCategory(category))
			.doOnNext(c -> LOG.info("Category inserted: " + c.getId() + " " + c.getName()))
			.thenMany(
				Flux.just(new Product("TV Panasonic Pantalla LCD", 456.89, electronics),
								new Product("Sony Camara HD Digital", 177.89, electronics),
								new Product("Apple iPod", 46.89, electronics),
								new Product("Sony Notebook", 846.89, computers),
								new Product("Hewlett Packard Multifuncional", 200.89, computers),
								new Product("Bianchi Bicicleta", 70.89, sports),
								new Product("HP Notebook Omen 17", 2500.89, computers),
								new Product("Mica Comoda 5 Cajones", 150.89, electronics),
								new Product("TV Sony Bravia OLED 4K Ultra HD", 2255.89, electronics)
						)
						.flatMap(product -> {
							product.setCreatedAt(new Date());
							return productService.save(product);
						}) // Better than using map to return a Flux<Product>
			).subscribe(product -> LOG.info("Insert: " + product.getId() + " " + product.getName()));
	}
}
