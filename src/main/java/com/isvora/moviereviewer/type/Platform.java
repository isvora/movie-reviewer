package com.isvora.moviereviewer.type;

public enum Platform {

    IMDB("IMDB"),
    METACRITIC("Metacritic"),
    ROTTEN_TOMATOES("Rotten Tomatoes");

    private String name;

    Platform(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
