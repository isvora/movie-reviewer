package com.isvora.moviereviewer.services;

import com.isvora.moviereviewer.database.MovieEntity;
import com.isvora.moviereviewer.repositories.MovieRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional()
    public List<MovieEntity> getMovies(String movie) {
        if (movie == null) {
            return movieRepository.findAll();
        }
        var movieEntity = movieRepository.findByName(movie);
        return movieEntity.map(List::of).orElse(Collections.emptyList());
    }

    public Optional<MovieEntity> getMovieByName(String movie) {
        return movieRepository.findByName(movie);
    }

    public MovieEntity addMovie(String movie) {
        var movieEntity = movieRepository.findByName(movie);
        return movieEntity.orElseGet(() -> movieRepository.save(new MovieEntity(movie)));
    }
}
