package ru.yjailbir.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yjailbir.data.model.Post;
import ru.yjailbir.data.service.PostsService;
import ru.yjailbir.util.ImagesUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequestMapping("/posts")
public class PostsController {
    private final PostsService postsService;
    @Value("${values.img_folder}")
    private String imgFolder;

    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Path.of(imgFolder));
    }

    @GetMapping
    public String getPosts(
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            Model model
    ) {
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", postsService.getPagesCount(pageSize));

        return "posts";
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
}
