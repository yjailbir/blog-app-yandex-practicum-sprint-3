package ru.yjailbir.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yjailbir.data.model.Comment;
import ru.yjailbir.data.model.Post;
import ru.yjailbir.data.service.PostsService;
import ru.yjailbir.util.ImagesUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class BlogController {
    private final PostsService postsService;
    @Value("${values.img_folder}")
    private String imgFolder;

    @Autowired
    public BlogController(PostsService postsService) {
        this.postsService = postsService;
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Path.of(imgFolder));
    }

    @GetMapping
    public String getPosts(
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(name = "search", defaultValue = "") String tag,
            Model model
    ) {
        model.addAttribute("tag", tag);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", postsService.getPagesCount(pageSize));

        List<Post> posts = postsService.getPosts(pageSize, (pageNumber - 1) * pageSize, tag);

        model.addAttribute("posts", posts);

        return "posts";
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") int id, Model model) {
        model.addAttribute("post", postsService.getPost(id));

        return "post";
    }

    @GetMapping("/add")
    public String getAddPostPage(@ModelAttribute("post") Post post, Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("buttonText", "Сохранить");

        return "add-post";
    }

    @PostMapping("/add")
    public String addPost(
            @ModelAttribute("post") Post post,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        if (image != null && !image.isEmpty()) {
            String imageUrl = ImagesUtil.saveImage(image.getResource(), imgFolder);
            post.setImgUrl(imageUrl);
        }

        postsService.save(post);

        return "redirect:/posts";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePostPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("buttonText", "Редактировать");
        model.addAttribute("post", postsService.getPost(id));

        return "add-post";
    }

    @PostMapping("/update/{id}")
    public String updatePost(
            @PathVariable("id") int id,
            @ModelAttribute("post") Post post,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        if (image != null && !image.isEmpty()) {
            String imageUrl = ImagesUtil.saveImage(image.getResource(), imgFolder);
            post.setImgUrl(imageUrl);
        }

        postsService.update(post);

        return "redirect:/posts/" + id;
    }

    @PostMapping("/update-likes/{id}")
    public String changeLikes(
            @PathVariable("id") int id,
            @RequestParam("like") Boolean isIncrement
    ) {
        postsService.changeLikes(id, isIncrement);

        return "redirect:/posts/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") int id) {
        postsService.deletePost(id);

        return "redirect:/posts";
    }

    @PostMapping("/comment/{id}")
    public String commentPost(@PathVariable("id") int id, @RequestParam("text") String text) {
        Comment comment = new Comment();
        comment.setPostId(id);
        comment.setComment(text);

        postsService.commentPost(comment);

        return "redirect:/posts/" + id;
    }

    @PostMapping("/delete/comment/{postId}/{commentId}")
    public String deleteComment(
            @PathVariable("postId") int postId,
            @PathVariable("commentId") int commentId
    ) {
        postsService.deleteComment(commentId);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/update-comment/{postId}/{commentId}")
    public String updateComment(
            @PathVariable("postId") int postId,
            @PathVariable("commentId") int commentId,
            @RequestParam("text") String text
    ) {
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setPostId(postId);
        comment.setComment(text);

        postsService.updateComment(comment);

        return "redirect:/posts/" + postId;
    }
}
