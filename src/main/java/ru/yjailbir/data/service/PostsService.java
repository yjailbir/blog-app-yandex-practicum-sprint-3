package ru.yjailbir.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yjailbir.data.model.Comment;
import ru.yjailbir.data.model.Post;
import ru.yjailbir.data.repository.CommentsRepository;
import ru.yjailbir.data.repository.LikesRepository;
import ru.yjailbir.data.repository.PostsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final LikesRepository likesRepository;
    private final CommentsRepository commentsRepository;

    @Autowired
    public PostsService(
            PostsRepository postsRepository,
            LikesRepository likesRepository,
            CommentsRepository commentsRepository
    ) {
        this.postsRepository = postsRepository;
        this.likesRepository = likesRepository;
        this.commentsRepository = commentsRepository;
    }

    public void save(Post post) {
        postsRepository.addPost(post);
        likesRepository.initNewPostLikes();
    }

    public void update(Post post) {
        postsRepository.updatePost(post);
    }

    public List<Post> getPosts(int count, int offset, String tag) {
        List<Post> posts = postsRepository.getPosts(count, offset, tag);

        List<Post> readyPosts = new ArrayList<>();

        posts.forEach(post -> readyPosts.add(fillPostInfo(post)));

        return readyPosts;
    }

    private Post fillPostInfo(Post post) {
        post.setTagList(new ArrayList<>());

        List.of(post.getTags().split(" ")).forEach(postTag -> post.getTagList().add(postTag.trim()));

        post.setLikesCount(
                likesRepository.getLikesCountByPostId(post.getId())
        );

        post.setCommentsCount(
                commentsRepository.getCommentsCountByPostId(post.getId())
        );

        post.setParagraphs(List.of(post.getText().split("\\n")));

        post.setComments(
                commentsRepository.getCommentsByPostId(post.getId())
        );

        return post;
    }

    public int getPagesCount(int onePagePostsCount) {
        Integer postsCount = postsRepository.getPostsCount();

        return postsCount % onePagePostsCount == 0 ? postsCount / onePagePostsCount : postsCount / onePagePostsCount + 1;
    }

    public Post getPost(int postId) {
        Post post = postsRepository.getPostById(postId);

        if (post != null)
            return fillPostInfo(post);
        else
            return null;
    }

    public void changeLikes(int postId, boolean isIncrement) {
        likesRepository.updateLikesByPostId(postId, isIncrement);
    }

    public void deletePost(int postId) {
        likesRepository.deleteByPostId(postId);
        commentsRepository.deleteAllCommentsByPostId(postId);
        postsRepository.deletePostById(postId);
    }

    public void commentPost(Comment comment) {
        commentsRepository.addComment(comment);
    }

    public void deleteComment(Integer id){
        commentsRepository.deleteByCommentId(id);
    }

    public void updateComment(Comment comment) {
        commentsRepository.updateComment(comment);
    }
}
