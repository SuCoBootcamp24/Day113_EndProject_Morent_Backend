package de.morent.backend.exceptions;

import de.morent.backend.entities.User;


public class UserAlreadyExistsException  extends RuntimeException{
    private final User user;

    public UserAlreadyExistsException(User user) {
        super("Host with the mail " + user.getEmail() + " already exists!");
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
