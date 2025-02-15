package de.morent.backend.services;

import de.morent.backend.entities.Newsletter;
import de.morent.backend.repositories.NewsRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewsService {

    private NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public void addToNewsletter(String firstName, String lastName, String email, Boolean isRegistered) {
        Optional<Newsletter> existingNewsCustomer = getCustomerByEmail(email);
        if (existingNewsCustomer.isPresent())
            throw new EntityExistsException("Email already registered in newsletter");

        Newsletter newCustomer = new Newsletter();
        newCustomer.setFirstName(firstName);
        newCustomer.setLastName(lastName);
        newCustomer.setEmail(email);
        if (isRegistered != null)
            newCustomer.setRegistered(isRegistered);

        newsRepository.save(newCustomer);
    }

    private Optional<Newsletter> getCustomerByEmail(String email) {
        return newsRepository.findByEmail(email);
    }

    public void deleteFromNewsletter(String email) {
        Optional<Newsletter> existingNewsCustomer = getCustomerByEmail(email);
        if (existingNewsCustomer.isEmpty()) return;
        newsRepository.delete(existingNewsCustomer.get());
    }

    public Boolean getNewsletterRegisteredStatus(String email) {
        Optional<Newsletter> existingNewsCustomer = getCustomerByEmail(email);
        return existingNewsCustomer.isPresent();
    }
}
