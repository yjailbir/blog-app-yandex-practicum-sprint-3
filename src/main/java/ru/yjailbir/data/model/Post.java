package ru.yjailbir.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Post {
    private int id;
    private String title;
    private String text;
    private List<String> paragraphs;
    private String imgUrl;
    private String tags;
    private List<String> tagList;
    private Integer likesCount;
    private Integer commentsCount;
    private List<Comment> comments;
}
