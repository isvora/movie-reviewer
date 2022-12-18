package com.isvora.moviereviewer.type;

public enum Source {

    AUDIENCE("Audience"),
    CRITIC("Critic");

    private String name;

    Source(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
