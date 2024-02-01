package com.cristhianvg.reactor_v1.reactorv1.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private User user;
    private Comments comments;
}
