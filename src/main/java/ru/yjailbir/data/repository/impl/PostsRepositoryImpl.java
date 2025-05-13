package ru.yjailbir.data.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yjailbir.data.model.Post;
import ru.yjailbir.data.repository.PostsRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostsRepositoryImpl implements PostsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> getPosts(int count, int offset) {
        List<Post> posts = jdbcTemplate.query(
                "SELECT * FROM posts ORDER BY id LIMIT ? OFFSET ?",
                new BeanPropertyRowMapper<>(Post.class),
                count, offset
        );

        posts.forEach(post -> {
            post.setTagList(new ArrayList<>());

            List.of(post.getTags().split(" ")).forEach(postTag -> {
                post.getTagList().add(postTag.trim());
                post.setLikes(
                        jdbcTemplate.queryForObject(
                                "SELECT likes FROM likes WHERE post_id = ?",
                                Integer.class,
                                post.getId()
                        )
                );
                post.setComments(
                        jdbcTemplate.queryForObject(
                                "SELECT COUNT(id) FROM comments WHERE post_id = ?",
                                Integer.class,
                                post.getId()
                        )
                );
            });
        });

        return posts;
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

        jdbcTemplate.update(
                "INSERT INTO likes(post_id, likes) VALUES (?, ?)",
                jdbcTemplate.queryForObject("SELECT last_value FROM posts_id_seq", Integer.class),
                0
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
}
