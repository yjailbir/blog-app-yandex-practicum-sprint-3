import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yjailbir.data.model.Comment;
import ru.yjailbir.data.model.Post;
import ru.yjailbir.data.repository.CommentsRepository;
import ru.yjailbir.data.repository.LikesRepository;
import ru.yjailbir.data.repository.PostsRepository;
import ru.yjailbir.data.service.PostsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostsServiceTest {

    @Mock
    private PostsRepository postsRepository;
    @Mock
    private LikesRepository likesRepository;
    @Mock
    private CommentsRepository commentsRepository;

    @InjectMocks
    private PostsService postsService;

    private Post testPOst;

    @BeforeEach
    void setUp() {
        testPOst = new Post();
        testPOst.setId(1);
        testPOst.setText("Тестовый текст поста\nВторая строка");
        testPOst.setTags("тег1 тег2");
    }

    @Test
    void testSave() {
        postsService.save(testPOst);
        verify(postsRepository).addPost(testPOst);
        verify(likesRepository).initNewPostLikes();
    }

    @Test
    void testUpdate() {
        postsService.update(testPOst);
        verify(postsRepository).updatePost(testPOst);
    }

    @Test
    void testGetPosts() {
        when(postsRepository.getPosts(2, 0, "тег1")).thenReturn(List.of(testPOst));
        when(likesRepository.getLikesCountByPostId(1)).thenReturn(5);
        when(commentsRepository.getCommentsCountByPostId(1)).thenReturn(3);
        when(commentsRepository.getCommentsByPostId(1)).thenReturn(List.of());

        List<Post> posts = postsService.getPosts(2, 0, "тег1");
        assertEquals(1, posts.size());
        assertEquals(5, posts.getFirst().getLikesCount());
        assertEquals(3, posts.getFirst().getCommentsCount());
        assertEquals(List.of("Тестовый текст поста", "Вторая строка"), posts.getFirst().getParagraphs());
    }

    @Test
    void testGetPostFound() {
        when(postsRepository.getPostById(1)).thenReturn(testPOst);
        when(likesRepository.getLikesCountByPostId(1)).thenReturn(5);
        when(commentsRepository.getCommentsCountByPostId(1)).thenReturn(2);
        when(commentsRepository.getCommentsByPostId(1)).thenReturn(List.of());

        Post post = postsService.getPost(1);
        assertNotNull(post);
        assertEquals(5, post.getLikesCount());
    }

    @Test
    void testGetPostNotFound() {
        when(postsRepository.getPostById(999)).thenReturn(null);
        Post post = postsService.getPost(999);
        assertNull(post);
    }

    @Test
    void testGetPagesCount() {
        when(postsRepository.getPostsCount()).thenReturn(15);
        assertEquals(3, postsService.getPagesCount(5));
    }

    @Test
    void testChangeLikes() {
        postsService.changeLikes(1, true);
        verify(likesRepository).updateLikesByPostId(1, true);
    }

    @Test
    void testDeletePost() {
        postsService.deletePost(1);
        verify(likesRepository).deleteByPostId(1);
        verify(commentsRepository).deleteAllCommentsByPostId(1);
        verify(postsRepository).deletePostById(1);
    }

    @Test
    void testCommentPost() {
        Comment comment = new Comment();
        postsService.commentPost(comment);
        verify(commentsRepository).addComment(comment);
    }

    @Test
    void testDeleteComment() {
        postsService.deleteComment(10);
        verify(commentsRepository).deleteByCommentId(10);
    }

    @Test
    void testUpdateComment() {
        Comment comment = new Comment();
        postsService.updateComment(comment);
        verify(commentsRepository).updateComment(comment);
    }
}