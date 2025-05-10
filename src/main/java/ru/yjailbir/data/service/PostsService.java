package ru.yjailbir.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yjailbir.data.model.Post;
import ru.yjailbir.data.repository.PostsRepository;

import java.util.List;

@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Autowired
    public PostsService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }
    public void save(Post post) {
        postsRepository.addPost(post);
    }

    public void update(Post post) {
        postsRepository.updatePost(post);
    }

    public List<Post> getPosts(int count, int offset) {
        return postsRepository.getPosts(count, offset);
    }
}
