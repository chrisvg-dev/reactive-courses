package com.cristhianvg.reactor_v1.reactorv1.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Comments {
    private List<String> comments;

    public Comments() {
        this.comments = new ArrayList<>();
    }

    public void addComment(String comment) {
        this.comments.add(comment);
    }
}
