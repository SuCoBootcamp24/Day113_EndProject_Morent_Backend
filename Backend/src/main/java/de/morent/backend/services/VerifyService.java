package de.morent.backend.services;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerifyService {

    private final MailService mailService;
    private final RedisService redisService;

    public VerifyService(MailService mailService, RedisService redisService) {
        this.mailService = mailService;
        this.redisService = redisService;
    }

    public String createVerifyCode() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        return "" + randomNumber;
    }

    public void sendVerifyMail(String email) {
        String verifyCode = createVerifyCode();
        mailService.sendVerifyEmail(email, verifyCode);
        redisService.saveVerifyCode(verifyCode, email);
    }

}
