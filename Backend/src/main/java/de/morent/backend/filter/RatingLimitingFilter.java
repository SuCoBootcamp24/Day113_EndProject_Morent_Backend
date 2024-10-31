package de.morent.backend.filter;

import de.morent.backend.services.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RatingLimitingFilter extends OncePerRequestFilter {
    private final RedisService redisService;
    private final Integer MAX_REQUESTS = 5;
    private final long TIMEOUT_MINUTES = 2;

    public RatingLimitingFilter(RedisService redisService) {
        this.redisService = redisService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String clientId = request.getRemoteAddr();

        if (isRateLimitExceeded(clientId)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too Many Requests - Rate limit of " + MAX_REQUESTS + " exceeded.");
            return;
        }
        filterChain.doFilter(request, response);
    }


    public boolean isRateLimitExceeded(String clientId) {
        Long countValue = redisService.getClientRequestCount(clientId);
        if (countValue == null) {
            redisService.saveClientRequestCount(clientId, 1L, TIMEOUT_MINUTES);
            return false;
        }
        
        redisService.saveClientRequestCount(clientId, countValue + 1L, TIMEOUT_MINUTES);

        return countValue + 1L >= MAX_REQUESTS;
    }


}
