package com.cristhianvg.reactor_v1.springwebfluxv1.models.documents;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @NotEmpty
    private String id;
    @NotBlank
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
