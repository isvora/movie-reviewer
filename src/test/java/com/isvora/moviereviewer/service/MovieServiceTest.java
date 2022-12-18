package com.isvora.moviereviewer.service;

import com.isvora.moviereviewer.TestHelper;
import com.isvora.moviereviewer.database.MovieEntity;
import com.isvora.moviereviewer.repositories.MovieRepository;
import com.isvora.moviereviewer.services.MovieService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    private MovieService movieService;

    @BeforeEach
    void init() {
        movieService = new MovieService(movieRepository);
    }

    @Test
    void testGetAllMovies() {
        // given
        List<MovieEntity> movieEntities = new ArrayList<>();
        movieEntities.add(new MovieEntity(TestHelper.MOVIE_NAME));

        Mockito.when(movieRepository.findAll()).thenReturn(movieEntities);

        // when
        var movies = movieService.getMovies(null);

        // then
        Assertions.assertFalse(movies.isEmpty());
        Assertions.assertEquals(movies.size(), 1);
        Assertions.assertEquals(movies.get(0).getName(), TestHelper.MOVIE_NAME);
    }

    @Test
    void testGetMovie() {
        // given
        var movieEntity = new MovieEntity(TestHelper.MOVIE_NAME);

        Mockito.when(movieRepository.findByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.of(movieEntity));

        // when
        var movies = movieService.getMovies(TestHelper.MOVIE_NAME);

        // then
        Assertions.assertFalse(movies.isEmpty());
        Assertions.assertEquals(movies.size(), 1);
        Assertions.assertEquals(movies.get(0).getName(), TestHelper.MOVIE_NAME);
    }

    @Test
    void testGetMovieNotFound() {
        // given
        Mockito.when(movieRepository.findByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.empty());

        // when
        var movies = movieService.getMovies(TestHelper.MOVIE_NAME);

        // then
        Assertions.assertTrue(movies.isEmpty());
    }

    @Test
    void testGetMovieByName() {
        // given
        var movieEntity = new MovieEntity(TestHelper.MOVIE_NAME);

        Mockito.when(movieRepository.findByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.of(movieEntity));

        // when
        var movie = movieService.getMovieByName(TestHelper.MOVIE_NAME);

        // then
        Assertions.assertTrue(movie.isPresent());
        Assertions.assertEquals(movie.get().getName(), TestHelper.MOVIE_NAME);
    }

    @Test
    void testGetMovieByNameNotFound() {
        // given
        Mockito.when(movieRepository.findByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.empty());

        // when
        var movie = movieService.getMovieByName(TestHelper.MOVIE_NAME);

        // then
        Assertions.assertFalse(movie.isPresent());
    }

    @Test
    void testAddMovie() {
        // given
        var movieEntity = new MovieEntity(TestHelper.MOVIE_NAME);

        Mockito.when(movieRepository.findByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.empty());
        Mockito.when(movieRepository.save(Mockito.any(MovieEntity.class))).thenReturn(movieEntity);

    // when
        var movie = movieService.addMovie(TestHelper.MOVIE_NAME);

        // then
        Assertions.assertNotNull(movie);
        Assertions.assertEquals(movie.getName(), TestHelper.MOVIE_NAME);
    }

}
