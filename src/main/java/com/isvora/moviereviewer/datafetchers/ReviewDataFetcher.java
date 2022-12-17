package com.isvora.moviereviewer.datafetchers;

import com.isvora.moviereviewer.database.ReviewEntity;
import com.isvora.moviereviewer.mapper.ReviewMapper;
import com.isvora.moviereviewer.services.ReviewService;
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
import java.util.stream.Collectors;

@DgsComponent
public class ReviewDataFetcher {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    public ReviewDataFetcher(ReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @DgsQuery
    public List<Review> reviews(@InputArgument String movie, @InputArgument String platform) {
        var reviewEntities = reviewService.getReviewsByMovieAndPlatform(movie, platform);
        return reviewMapper.toReviews(new HashSet<>(reviewEntities));
    }

    @DgsMutation
    public Review addReview(@InputArgument ReviewInput reviewInput) {
       return reviewMapper.toReview(reviewService.addReview(reviewInput));
    }
}
