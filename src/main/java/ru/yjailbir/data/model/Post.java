package ru.yjailbir.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Post {
    private int id;
    private String title;
    private String text;
    private String imgUrl;
    private String tags;
}
