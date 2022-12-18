package com.isvora.moviereviewer.datafetchers;

import com.isvora.moviereviewer.mapper.ReviewMapper;
import com.isvora.moviereviewer.services.ImdbService;
import com.isvora.moviereviewer.services.ReviewService;
import com.isvora.moviereviewer.validation.ReviewValidator;
import com.netflix.dgs.codegen.generated.types.Review;
import com.netflix.dgs.codegen.generated.types.ReviewInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.HashSet;
import java.util.List;

@DgsComponent
public class ReviewDataFetcher {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final ReviewValidator reviewValidator;
    private final ImdbService imdbService;

    public ReviewDataFetcher(ReviewService reviewService, ReviewMapper reviewMapper, ReviewValidator reviewValidator, ImdbService imdbService) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
        this.reviewValidator = reviewValidator;
        this.imdbService = imdbService;
    }

    @DgsQuery
    public List<Review> reviews(@InputArgument String movie, @InputArgument String platform) {
        var reviewEntities = reviewService.getReviewsByMovieAndPlatform(movie, platform);
        return reviewMapper.toReviews(new HashSet<>(reviewEntities));
    }

    @DgsMutation
    public Review addReview(@InputArgument ReviewInput reviewInput) {
        var reviewError = reviewValidator.validateReview(reviewInput.getMovie());
        if (!reviewError.getError().isEmpty()) {
            return reviewError;
        }
        return reviewMapper.toReview(reviewService.addReview(reviewInput));
    }

    @DgsMutation
    public List<Review> scrapeRatings(@InputArgument String movie) {
        var reviewError = reviewValidator.validateReview(movie);
        if (!reviewError.getError().isEmpty()) {
            return List.of(reviewError);
        }
        var rating = imdbService.searchRating(movie);
        var reviewEntity = reviewService.addReview(reviewMapper.ratingToReviewInput(rating));
        return List.of(reviewMapper.toReview(reviewEntity));
    }
}
