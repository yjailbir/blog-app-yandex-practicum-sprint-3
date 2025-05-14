package ru.yjailbir.data.repository;

import ru.yjailbir.data.model.Comment;

import java.util.List;

public interface CommentsRepository {
    Integer getCommentsCountByPOstId(Integer postId);
    List<Comment> getCommentsByPostId(Integer postId);
}
