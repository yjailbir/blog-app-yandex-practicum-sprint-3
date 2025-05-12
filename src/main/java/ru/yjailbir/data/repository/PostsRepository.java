package ru.yjailbir.data.repository;

import ru.yjailbir.data.model.Post;

import java.util.List;

public interface PostsRepository {
    List<Post> getPosts(int count, int offset);
    void addPost(Post post);
    void updatePost(Post post);
    Integer getPostsCount();
}
