package com.cristhianvg.reactor_v1.springwebfluxv1.models.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Document(collection = "products") // BSON collection name
public class Product {
    @Id
    private String id;
    private String name;
    private Double price;
    private Date createdAt;

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }
}
