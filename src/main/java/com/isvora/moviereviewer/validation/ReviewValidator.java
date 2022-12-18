package com.isvora.moviereviewer.validation;

import com.isvora.moviereviewer.services.MovieService;
import com.netflix.dgs.codegen.generated.types.ReviewError;
import com.netflix.dgs.codegen.generated.types.ReviewErrorEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewValidator {

    private final MovieService movieService;

    public ReviewValidator(MovieService movieService) {
        this.movieService = movieService;
    }

    public ReviewError validateReview(String movie) {
        List<ReviewErrorEnum> errors = new ArrayList<>();

        var movieEntity = movieService.getMovieByName(movie);
        if (movieEntity.isEmpty()) {
            errors.add(ReviewErrorEnum.MOVIE_NOT_FOUND);
        }

        return ReviewError.newBuilder().error(errors).build();
    }
}
