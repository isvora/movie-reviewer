package com.isvora.moviereviewer.datafetchers;

import com.isvora.moviereviewer.database.ReviewEntity;
import com.isvora.moviereviewer.mapper.ReviewMapper;
import com.isvora.moviereviewer.model.Rating;
import com.isvora.moviereviewer.services.ImdbService;
import com.isvora.moviereviewer.services.MetacriticService;
import com.isvora.moviereviewer.services.ReviewService;
import com.isvora.moviereviewer.validation.ReviewValidator;
import com.netflix.dgs.codegen.generated.types.Review;
import com.netflix.dgs.codegen.generated.types.ReviewInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DgsComponent
public class ReviewDataFetcher {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final ReviewValidator reviewValidator;
    private final ImdbService imdbService;
    private final MetacriticService metacriticService;

    public ReviewDataFetcher(ReviewService reviewService, ReviewMapper reviewMapper, ReviewValidator reviewValidator, ImdbService imdbService, MetacriticService metacriticService) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
        this.reviewValidator = reviewValidator;
        this.imdbService = imdbService;
        this.metacriticService = metacriticService;
    }

    @DgsQuery
    public List<Review> reviews(@InputArgument String movie, @InputArgument String platform) {
        var reviewEntities = reviewService.getReviewsByMovieAndPlatform(movie, platform);
        return reviewMapper.toReviews(new HashSet<>(reviewEntities));
    }

    @DgsMutation
    public Review addReview(@InputArgument ReviewInput reviewInput) {
        var reviewErrorReviewValidationResponse = reviewValidator.validateReview(reviewInput.getMovie());
        if (reviewErrorReviewValidationResponse.isSuccess()) {
            return reviewMapper.toReview(reviewService.addReview(reviewInput, reviewInput.getSource()));
        } else {
            return reviewErrorReviewValidationResponse.getK();
        }
    }

    @DgsMutation
    public List<Review> scrapeRatings(@InputArgument String movie) {
        var reviewErrorReviewValidationResponse = reviewValidator.validateReview(movie);
        if (reviewErrorReviewValidationResponse.isSuccess()) {
            List<Rating> ratings = new ArrayList<>();
            Set<ReviewEntity> reviewEntities = new HashSet<>();

            ratings.add(metacriticService.searchCriticRating(movie));
            ratings.add(imdbService.searchRating(movie));
            ratings.forEach(rating -> {
                var reviewError = reviewValidator.validateRatings(reviewErrorReviewValidationResponse.getT(), rating);
                if (reviewError.getErrors().isEmpty()) {
                    reviewEntities.add(reviewService.addReview(reviewMapper.ratingToReviewInput(rating), rating.getSource().getName()));
                }
            });

            return reviewMapper.toReviews(reviewEntities);
        } else {
            return List.of(reviewErrorReviewValidationResponse.getK());
        }
    }
}
