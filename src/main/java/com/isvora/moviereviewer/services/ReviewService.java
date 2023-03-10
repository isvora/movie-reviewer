package com.isvora.moviereviewer.services;

import com.isvora.moviereviewer.database.ReviewEntity;
import com.isvora.moviereviewer.repositories.ReviewRepository;
import com.netflix.dgs.codegen.generated.types.ReviewInput;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ReviewService {

    private final MovieService movieService;
    private final ReviewRepository reviewRepository;

    public ReviewService(MovieService movieService, ReviewRepository reviewRepository) {
        this.movieService = movieService;
        this.reviewRepository = reviewRepository;
    }

    public ReviewEntity addReview(ReviewInput reviewInput, String source) {
        var movie = movieService.getMovieByName(reviewInput.getMovie());
        return reviewRepository.save(new ReviewEntity(reviewInput.getScore(), reviewInput.getPlatform(), source, movie.get()));
    }

    public List<ReviewEntity> getReviewsByMovieAndPlatform(String movie, String platform) {
        if (platform == null) {
            return getReviewsByMovie(movie);
        } else {
            var movieEntity = movieService.getMovieByName(movie);
            if (movieEntity.isPresent()) {
                var review = reviewRepository.findByMovieEntityAndPlatform(movieEntity.get(), platform);
                return review.map(List::of).orElse(Collections.emptyList());
            } else {
                return Collections.emptyList();
            }

        }
    }

    public List<ReviewEntity> getReviewsByMovie(String movie) {
        var movieEntity = movieService.getMovieByName(movie);
        if (movieEntity.isPresent()) {
            return reviewRepository.findAllByMovieEntity(movieEntity.get());
        } else {
            return Collections.emptyList();
        }
    }
}
