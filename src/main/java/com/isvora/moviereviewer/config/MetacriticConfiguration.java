package com.isvora.moviereviewer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetacriticConfiguration {

    public MetacriticConfiguration() {
    }

    @Value("${metacritic.url}")
    private String url;

    public String getUrl() {
        return url;
    }
}
