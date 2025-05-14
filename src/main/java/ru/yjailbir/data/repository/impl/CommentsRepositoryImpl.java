package ru.yjailbir.data.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yjailbir.data.model.Comment;
import ru.yjailbir.data.repository.CommentsRepository;

import java.util.List;

@Repository
public class CommentsRepositoryImpl implements CommentsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer getCommentsCountByPOstId(Integer postId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(id) FROM comments WHERE post_id = ?",
                Integer.class,
                postId
        );
    }

    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        return jdbcTemplate.query(
                "SELECT * FROM comments WHERE post_id = ?",
                new BeanPropertyRowMapper<>(Comment.class),
                postId
        );
    }
}
