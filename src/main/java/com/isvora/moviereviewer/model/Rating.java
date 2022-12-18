package com.isvora.moviereviewer.model;

import com.isvora.moviereviewer.type.Platform;

public class Rating {

    private double score;

    private Platform platform;

    private String movie;

    public Rating() {
    }

    public Rating(double score, Platform platform, String movie) {
        this.score = score;
        this.platform = platform;
        this.movie = movie;
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
}
