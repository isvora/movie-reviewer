package com.isvora.moviereviewer.services;

import com.isvora.moviereviewer.config.MetacriticConfiguration;
import com.isvora.moviereviewer.model.Rating;
import com.isvora.moviereviewer.type.Platform;
import com.isvora.moviereviewer.type.Source;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MetacriticService {

    private final MetacriticConfiguration metacriticConfiguration;

    public MetacriticService(MetacriticConfiguration metacriticConfiguration) {
        this.metacriticConfiguration = metacriticConfiguration;
    }

    public Rating searchCriticRating(String movie) {
        return getRating(movie, "#nav_to_metascore > div:nth-child(2) > div.distribution > div.score.fl > a > div", Source.CRITIC);
    }


    public Rating searchAudienceRating(String movie) {
        return getRating(movie, "#nav_to_metascore > div:nth-child(3) > div.distribution > div.score.fl > a > div", Source.AUDIENCE);
    }

    private Rating getRating(String movie, String cssQuery, Source source) {
        String metacriticMovie = movie.trim()
                .replaceAll(" ", "-")
                .replaceAll("'", "")
                .replaceAll("/[^a-z\\d\\?!\\-]/", "")
                .toLowerCase();

        String url = String.format(metacriticConfiguration.getUrl(), metacriticMovie);
        try {
            Document doc = Jsoup.connect(url).get();
            var metacriticScore = doc.select(cssQuery);
            var score = ((TextNode) metacriticScore.get(0).childNodes().get(0)).text();
            return new Rating(Double.parseDouble(score), Platform.METACRITIC, movie, source);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
