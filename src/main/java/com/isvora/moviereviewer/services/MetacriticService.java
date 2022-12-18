package com.isvora.moviereviewer.services;

import com.isvora.moviereviewer.config.MetacriticConfiguration;
import com.isvora.moviereviewer.model.Rating;
import com.isvora.moviereviewer.type.Platform;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MetacriticService extends ScraperService {

    private final MetacriticConfiguration metacriticConfiguration;

    public MetacriticService(MetacriticConfiguration metacriticConfiguration) {
        this.metacriticConfiguration = metacriticConfiguration;
    }

    @Override
    public Rating searchRating(String movie) {
        String metacriticMovie = movie.trim()
                .replaceAll(" ", "-")
                .replaceAll("$ ", "")
                .replaceAll("/[^a-z\\d\\?!\\-]/", "")
                .toLowerCase();

        String url = String.format(metacriticConfiguration.getUrl(), metacriticMovie);
        try {
            Document doc = Jsoup.connect(url).get();
            var metacriticScore = doc.select("#nav_to_metascore > div:nth-child(2) > div.distribution > div.score.fl > a > div");
            var score = ((TextNode) metacriticScore.get(0).childNodes().get(0)).text();
            return new Rating(Double.parseDouble(score), Platform.METACRITIC, movie);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
