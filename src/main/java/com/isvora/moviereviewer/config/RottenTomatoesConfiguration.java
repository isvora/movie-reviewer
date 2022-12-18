package com.isvora.moviereviewer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RottenTomatoesConfiguration {

    public RottenTomatoesConfiguration() {
    }

    @Value("${rotten.tomatoes.url}")
    private String url;

    public String getUrl() {
        return url;
    }

}
