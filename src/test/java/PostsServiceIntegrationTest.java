import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yjailbir.config.ApplicationConfig;
import ru.yjailbir.data.model.Post;
import ru.yjailbir.data.service.PostsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApplicationConfig.class)
public class PostsServiceIntegrationTest {

    @Autowired
    private PostsService postsService;

    @Test
    void testSaveAndGetPost() {
        Post post = new Post();
        post.setTitle("Тест");
        post.setTags("Тег1 Тег2");
        post.setText("Абзац 1\nАбзац 2");

        postsService.save(post);

        List<Post> posts = postsService.getPosts(10, 0, "");
        assertFalse(posts.isEmpty());

        Post saved = posts.get(posts.size() - 1);
        assertEquals("Тест", saved.getTitle());
        assertEquals(List.of("Тег1", "Тег2"), saved.getTagList());
        assertEquals(List.of("Абзац 1", "Абзац 2"), saved.getParagraphs());
    }
}
