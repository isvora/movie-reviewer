package com.isvora.moviereviewer.datafetchers;

import com.isvora.moviereviewer.mapper.MovieMapper;
import com.isvora.moviereviewer.services.MovieService;
import com.netflix.dgs.codegen.generated.types.Movie;
import com.netflix.graphql.dgs.*;

import java.util.ArrayList;
import java.util.List;

@DgsComponent
public class MovieDataFetcher {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    public MovieDataFetcher(MovieService movieService, MovieMapper movieMapper) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
    }

    @DgsQuery
    public List<Movie> movies(@InputArgument String movie) {
        List<Movie> movieList = new ArrayList<>();
        var movies = movieService.getMovies(movie);
        movies.forEach(movieEntity -> movieList.add(movieMapper.toMovie(movieEntity)));
        return movieList;
    }

    @DgsMutation
    @DgsEnableDataFetcherInstrumentation(false)
    public Movie addMovie(@InputArgument String movie) {
        var movieEntity = movieService.addMovie(movie);
        return movieMapper.toMovie(movieEntity);
    }
}
