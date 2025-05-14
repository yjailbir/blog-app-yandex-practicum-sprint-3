package ru.yjailbir.data.repository;

import ru.yjailbir.data.model.Comment;

import java.util.List;

public interface CommentsRepository {
    void addComment(Comment comment);
    Integer getCommentsCountByPostId(Integer postId);
    List<Comment> getCommentsByPostId(Integer postId);
    void deleteByCommentId(Integer id);
    void deleteAllCommentsByPostId(Integer postId);
    void updateComment(Comment comment);
}
