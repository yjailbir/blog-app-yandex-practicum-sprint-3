package ru.yjailbir.data.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yjailbir.data.model.Post;
import ru.yjailbir.data.repository.PostsRepository;

import java.util.List;

@Repository
public class PostsRepositoryImpl implements PostsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> getPosts(int count, int offset, String tag) {
        if (tag.isEmpty())
            return jdbcTemplate.query(
                "SELECT * FROM posts ORDER BY id LIMIT ? OFFSET ?",
                new BeanPropertyRowMapper<>(Post.class),
                count, offset
        );
        else {
            String likePattern = "%" + tag + "%";
            return jdbcTemplate.query(
                    "SELECT * FROM posts WHERE tags ILIKE ? ORDER BY id LIMIT ? OFFSET ?",
                    new BeanPropertyRowMapper<>(Post.class),
                    likePattern, count, offset
            );
        }
    }

    @Override
    public void addPost(Post post) {
        jdbcTemplate.update(
                "INSERT INTO posts (title, text, img_url, tags) VALUES(?, ?, ?, ?)",
                post.getTitle(),
                post.getText(),
                post.getImgUrl(),
                post.getTags()
        );
    }

    @Override
    public void updatePost(Post post) {
        jdbcTemplate.update(
                "UPDATE posts SET title = ?, text = ?, img_url = ?, tags = ? WHERE id = ?",
                post.getTitle(),
                post.getText(),
                post.getImgUrl(),
                post.getTags(),
                post.getId()
        );
    }

    @Override
    public Integer getPostsCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts", Integer.class);
    }

    @Override
    public Post getPostById(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM posts WHERE id = ?",
                new BeanPropertyRowMapper<>(Post.class),
                id
        );
    }

    @Override
    public void deletePostById(int id) {
        jdbcTemplate.update(
                "DELETE FROM posts WHERE id = ?",
                id
        );
    }
}
