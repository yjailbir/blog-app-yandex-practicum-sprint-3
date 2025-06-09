import org.junit.jupiter.api.BeforeEach;
import ru.yjailbir.data.model.Comment;
import ru.yjailbir.data.model.Post;
import ru.yjailbir.data.repository.CommentsRepository;
import ru.yjailbir.data.repository.LikesRepository;
import ru.yjailbir.data.repository.PostsRepository;
import ru.yjailbir.data.service.PostsService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PostsServiceTest {

    private PostsRepository postsRepository;
    private LikesRepository likesRepository;
    private CommentsRepository commentsRepository;
    private PostsService postsService;

    @BeforeEach
    void setUp() {
        postsRepository = mock(PostsRepository.class);
        likesRepository = mock(LikesRepository.class);
        commentsRepository = mock(CommentsRepository.class);
        postsService = new PostsService(postsRepository, likesRepository, commentsRepository);
    }

    @Test
    void save_callsRepositoriesCorrectly() {
        Post post = new Post();
        postsService.save(post);

        verify(postsRepository).addPost(post);
        verify(likesRepository).initNewPostLikes();
    }

    @Test
    void getPost_fillsAllFields() {
        Post post = new Post();
        post.setId(1);
        post.setTags("Тег1 Тег2");
        post.setText("Абзац1\nАбзац2");

        when(postsRepository.getPostById(1)).thenReturn(post);
        when(likesRepository.getLikesCountByPostId(1)).thenReturn(5);
        when(commentsRepository.getCommentsCountByPostId(1)).thenReturn(2);
        when(commentsRepository.getCommentsByPostId(1)).thenReturn(List.of(new Comment(), new Comment()));

        Post result = postsService.getPost(1);

        assertEquals(List.of("Тег1", "Тег2"), result.getTagList());
        assertEquals(List.of("Абзац1", "Абзац2"), result.getParagraphs());
        assertEquals(5, result.getLikesCount());
        assertEquals(2, result.getCommentsCount());
        assertEquals(2, result.getComments().size());
    }
}
