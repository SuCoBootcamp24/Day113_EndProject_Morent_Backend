package de.morent.backend.controller;

import de.morent.backend.services.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    private NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }



    @GetMapping
    public ResponseEntity<Boolean> GetNewsletterRegisteredStatus(@RequestParam String email) {
        return new ResponseEntity<>(newsService.getNewsletterRegisteredStatus(email), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addToNewsletter(
                            @RequestParam String email,
                            @RequestParam String firstName,
                            @RequestParam String lastName,
                            @RequestParam(required = false) boolean isRegistered) {
        newsService.addToNewsletter(firstName, lastName, email, isRegistered);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllNewsletters(@RequestParam String email) {
        newsService.deleteFromNewsletter(email);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/startmail")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<Void> sendNewsletter(@RequestParam String email) {
        // Implement sending newsletter to the given email address
        return ResponseEntity.ok().build();
    }
}
