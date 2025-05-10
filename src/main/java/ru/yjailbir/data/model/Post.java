package ru.yjailbir.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Post {
    private int id;
    private String title;
    private String text;
    private String imgUrl;
}
