package de.morent.backend.configurations;

import de.morent.backend.filter.RatingLimitingFilter;
import de.morent.backend.services.RedisService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<RatingLimitingFilter> ratingFilter(RedisService redisService) {
        FilterRegistrationBean<RatingLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RatingLimitingFilter(redisService)); //
        registrationBean.addUrlPatterns("/api/**");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
