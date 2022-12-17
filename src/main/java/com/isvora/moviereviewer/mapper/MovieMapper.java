package com.isvora.moviereviewer.mapper;

import com.isvora.moviereviewer.database.MovieEntity;
import com.netflix.dgs.codegen.generated.types.Movie;
import org.springframework.stereotype.Service;

@Service
public class MovieMapper {

    private final ReviewMapper reviewMapper;

    public MovieMapper(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    public Movie toMovie(MovieEntity movieEntity) {
        return Movie.newBuilder()
                .name(movieEntity.getName())
                .reviews(reviewMapper.toReviews(movieEntity.getReviewEntities()))
                .build();
    }
}
