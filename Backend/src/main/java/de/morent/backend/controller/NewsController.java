package de.morent.backend.controller;

import de.morent.backend.services.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    private NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }


    @PostMapping
    public ResponseEntity<Void> addToNewsletter(
                            @RequestParam String firstName,
                            @RequestParam String lastName,
                            @RequestParam String email,
                            @RequestParam(required = false) boolean isRegistered) {
        newsService.addToNewsletter(firstName, lastName, email, isRegistered);
        return ResponseEntity.ok().build();

    }
}
