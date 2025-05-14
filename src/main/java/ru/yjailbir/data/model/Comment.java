package ru.yjailbir.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comment {
    private int id;
    private int postId;
    private String comment;
}
