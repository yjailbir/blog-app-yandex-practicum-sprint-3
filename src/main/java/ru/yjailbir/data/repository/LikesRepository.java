package ru.yjailbir.data.repository;

public interface LikesRepository {
    Integer getLikesCountByPostId(Integer postId);
    void updateLikesByPostId(int id, boolean increment);
    void initNewPostLikes();
}
