package com.isvora.moviereviewer.model;

import com.isvora.moviereviewer.type.Platform;
import com.isvora.moviereviewer.type.Source;

public class Rating {

    private double score;

    private Platform platform;

    private String movie;

    private Source source;

    public Rating() {
    }

    public Rating(double score, Platform platform, String movie, Source source) {
        this.score = score;
        this.platform = platform;
        this.movie = movie;
        this.source = source;
    }

    public double getScore() {
        return score;
    }

    public Platform getPlatform() {
        return platform;
    }

    public String getMovie() {
        return movie;
    }

    public Source getSource() {
        return source;
    }
}
