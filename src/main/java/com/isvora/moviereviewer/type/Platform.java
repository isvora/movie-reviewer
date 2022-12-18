package com.isvora.moviereviewer.type;

public enum Platform {

    IMDB("IMDB");

    private String name;

    Platform(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
