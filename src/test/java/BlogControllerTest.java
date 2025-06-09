import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yjailbir.config.ApplicationConfig;
import ru.yjailbir.data.model.Comment;
import ru.yjailbir.data.model.Post;
import ru.yjailbir.data.service.PostsService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ApplicationConfig.class)
@AutoConfigureMockMvc
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostsService postsService;

    private Post samplePost;

    @BeforeEach
    void setUp() {
        samplePost = new Post();
        samplePost.setId(1);
        samplePost.setTitle("Заголовок");
        samplePost.setText("Крутой текст\nИ второй крутой текст");
        samplePost.setTags("тег1 тег2");
        samplePost.setTagList(List.of(samplePost.getTags().split(" ")));
        samplePost.setComments(List.of());
        samplePost.setLikesCount(0);
        samplePost.setCommentsCount(0);
        samplePost.setParagraphs(List.of(samplePost.getTags().split("\n")));

        Comment sampleComment = new Comment();
        sampleComment.setId(1);
        sampleComment.setPostId(1);
        sampleComment.setComment("Комментарий к посту жесть");
    }

    @Test
    void shouldReturnPostsPage() throws Exception {
        when(postsService.getPagesCount(5)).thenReturn(1);
        when(postsService.getPosts(eq(5), eq(0), eq(""))).thenReturn(List.of(samplePost));

        mockMvc.perform(get("/posts")
                        .param("pageSize", "5")
                        .param("pageNumber", "1")
                        .param("search", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts", "tag", "pageSize", "pageNumber", "totalPages"));
    }

    @Test
    void shouldReturnPostById() throws Exception {
        when(postsService.getPost(1)).thenReturn(samplePost);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void shouldReturnAddPostPage() throws Exception {
        mockMvc.perform(get("/posts/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"))
                .andExpect(model().attributeExists("post", "buttonText"));
    }

    @Test
    void shouldAddPost() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "data".getBytes());

        mockMvc.perform(multipart("/posts/add")
                        .file(image)
                        .param("title", "Ещё пост")
                        .param("text", "Текст а где")
                        .param("tags", "Тег13213432"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(postsService).save(any(Post.class));
    }

    @Test
    void shouldReturnUpdatePostPage() throws Exception {
        when(postsService.getPost(1)).thenReturn(samplePost);

        mockMvc.perform(get("/posts/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"))
                .andExpect(model().attributeExists("post", "buttonText"));
    }

    @Test
    void shouldUpdatePost() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "data".getBytes());

        mockMvc.perform(multipart("/posts/update/1")
                        .file(image)
                        .param("id", "1")
                        .param("title", "Новый заголовок офигеть")
                        .param("text", "Новый текст вообще жесть")
                        .param("tags", "Тег0998765"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(postsService).update(any(Post.class));
    }

    @Test
    void shouldChangeLikes() throws Exception {
        mockMvc.perform(post("/posts/update-likes/1")
                        .param("like", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(postsService).changeLikes(1, true);
    }

    @Test
    void shouldDeletePost() throws Exception {
        mockMvc.perform(post("/posts/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(postsService).deletePost(1);
    }

    @Test
    void shouldAddComment() throws Exception {
        mockMvc.perform(post("/posts/comment/1")
                        .param("text", "Комментарий"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(postsService).commentPost(argThat(comment ->
                comment.getPostId() == 1 && comment.getComment().equals("Комментарий")));
    }

    @Test
    void shouldDeleteComment() throws Exception {
        mockMvc.perform(post("/posts/delete/comment/1/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(postsService).deleteComment(1);
    }

    @Test
    void shouldUpdateComment() throws Exception {
        mockMvc.perform(post("/posts/update-comment/1/1")
                        .param("text", "Комментарий переобулся"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(postsService).updateComment(argThat(comment ->
                comment.getId() == 1 && comment.getPostId() == 1 && comment.getComment().equals("Комментарий переобулся")));
    }
}
