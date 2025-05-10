package ru.yjailbir.data.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Post {
    private int id;
    private String title;
    private String text;
    private String imgUrl;
    private List<String> tags;
}
