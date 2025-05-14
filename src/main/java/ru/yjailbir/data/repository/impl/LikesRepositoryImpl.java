package ru.yjailbir.data.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yjailbir.data.repository.LikesRepository;

@Repository
public class LikesRepositoryImpl implements LikesRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Integer getLikesCountByPostId(Integer postId) {
        return jdbcTemplate.queryForObject(
                "SELECT likes FROM likes WHERE post_id = ?",
                Integer.class,
                postId
        );
    }

    @Override
    public void updateLikesByPostId(int id, boolean increment) {
        if (increment)
            jdbcTemplate.update("UPDATE likes SET likes = likes + 1 WHERE post_id = ?", id);
        else
            jdbcTemplate.update("UPDATE likes SET likes = likes - 1 WHERE post_id = ?", id);
    }

    @Override
    public void initNewPostLikes() {
        jdbcTemplate.update(
                "INSERT INTO likes(post_id, likes) VALUES (?, ?)",
                jdbcTemplate.queryForObject("SELECT last_value FROM posts_id_seq", Integer.class),
                0
        );
    }
}
