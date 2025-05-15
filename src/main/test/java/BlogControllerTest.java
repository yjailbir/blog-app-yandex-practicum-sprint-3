
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ru.yjailbir.controller.BlogController;
import ru.yjailbir.data.model.Post;
import ru.yjailbir.data.service.PostsService;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BlogControllerTest {

    private PostsService postsService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        postsService = mock(PostsService.class);
        BlogController blogController = new BlogController(postsService);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders
                .standaloneSetup(blogController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void testGetPosts() throws Exception {
        when(postsService.getPagesCount(5)).thenReturn(1);
        when(postsService.getPosts(5, 0, "")).thenReturn(List.of());

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"));
    }

    @Test
    void testGetPost() throws Exception {
        Post post = new Post();
        when(postsService.getPost(1)).thenReturn(post);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attribute("post", post));
    }

    @Test
    void testAddPost() throws Exception {
        mockMvc.perform(post("/posts/add").param("text", "Test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void testUpdatePost() throws Exception {
        mockMvc.perform(post("/posts/update/1").param("text", "Updated"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }

    @Test
    void testChangeLikes() throws Exception {
        mockMvc.perform(post("/posts/update-likes/1").param("like", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }

    @Test
    void testDeletePost() throws Exception {
        mockMvc.perform(post("/posts/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void testCommentPost() throws Exception {
        mockMvc.perform(post("/posts/comment/1").param("text", "Nice!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }

    @Test
    void testDeleteComment() throws Exception {
        mockMvc.perform(post("/posts/delete/comment/1/2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }

    @Test
    void testUpdateComment() throws Exception {
        mockMvc.perform(post("/posts/update-comment/1/2").param("text", "Updated comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }
}