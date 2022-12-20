package com.isvora.moviereviewer.services;

import com.isvora.moviereviewer.config.RottenTomatoesConfiguration;
import com.isvora.moviereviewer.model.Rating;
import com.isvora.moviereviewer.type.Platform;
import com.isvora.moviereviewer.type.Source;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RottenTomatoesService {

    private final RottenTomatoesConfiguration rottenTomatoesConfiguration;

    public RottenTomatoesService(RottenTomatoesConfiguration rottenTomatoesConfiguration) {
        this.rottenTomatoesConfiguration = rottenTomatoesConfiguration;
    }

    public Rating searchCriticRating(String movie) {
        return getRating(movie, "tomatometerscore", Source.CRITIC);
    }


    public Rating searchAudienceRating(String movie) {
        return getRating(movie, "audiencescore", Source.AUDIENCE);
    }

    private Rating getRating(String movie, String cssQuery, Source source) {
        String metacriticMovie = movie.trim()
                .replaceAll(" ", "_")
                .replaceAll("'", "")
                .replaceAll("/[^a-z\\d\\?!\\-]/", "")
                .toLowerCase();

        String url = String.format(rottenTomatoesConfiguration.getUrl(), metacriticMovie);
        try {
            Document doc = Jsoup.connect(url).get();
            var metacriticScore = doc.getElementsByAttribute("audiencescore").get(0).attributes().get(cssQuery);
            return new Rating(Double.parseDouble(metacriticScore), Platform.ROTTEN_TOMATOES, movie, source);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
