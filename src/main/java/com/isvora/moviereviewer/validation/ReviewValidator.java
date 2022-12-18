package com.isvora.moviereviewer.validation;

import com.isvora.moviereviewer.database.MovieEntity;
import com.isvora.moviereviewer.model.Rating;
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

    public ReviewValidationResponse<MovieEntity, ReviewError> validateReview(String movie) {
        List<ReviewErrorEnum> errors = new ArrayList<>();

        var movieEntity = movieService.getMovieByName(movie);
        if (movieEntity.isEmpty()) {
            errors.add(ReviewErrorEnum.MOVIE_NOT_FOUND);
            return new ReviewValidationResponse<>(ReviewError.newBuilder().errors(errors).build()) {
            };
        } else {
            return new ReviewValidationResponse<>(movieEntity.get(), true) {
            };
        }
    }

    public ReviewError validateRatings(MovieEntity movie, Rating rating) {
        List<ReviewErrorEnum> errors = new ArrayList<>();

        var reviewFound = movie.getReviewEntities().stream().filter(reviewEntity -> reviewEntity.getPlatform().equals(rating.getPlatform().getName()) && reviewEntity.getScore() == rating.getScore()).findAny();

        if (reviewFound.isPresent()) {
            errors.add(ReviewErrorEnum.REVIEW_ALREADY_PRESENT);
        }

        return ReviewError.newBuilder().errors(errors).build();
    }
}
