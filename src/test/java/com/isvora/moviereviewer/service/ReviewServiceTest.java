package com.isvora.moviereviewer.service;

import com.isvora.moviereviewer.TestHelper;
import com.isvora.moviereviewer.database.MovieEntity;
import com.isvora.moviereviewer.database.ReviewEntity;
import com.isvora.moviereviewer.repositories.ReviewRepository;
import com.isvora.moviereviewer.services.MovieService;
import com.isvora.moviereviewer.services.ReviewService;
import com.netflix.dgs.codegen.generated.types.ReviewInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private MovieService movieService;

    @Mock
    private ReviewRepository reviewRepository;

    private ReviewService reviewService;


    @BeforeEach
    void init() {
        reviewService = new ReviewService(movieService, reviewRepository);
    }

    @Test
    void testAddReview() {
        // given
        var reviewInput = ReviewInput.newBuilder()
                .platform(TestHelper.PLATFORM)
                .movie(TestHelper.MOVIE_NAME)
                .score(TestHelper.SCORE)
                .build();
        var movieEntity = new MovieEntity(TestHelper.MOVIE_NAME);
        var reviewEntity = new ReviewEntity(TestHelper.SCORE, TestHelper.PLATFORM, movieEntity);

        Mockito.when(movieService.getMovieByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.of(movieEntity));
        Mockito.when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(reviewEntity);

        // when
        var result = reviewService.addReview(reviewInput);

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getPlatform(), reviewInput.getPlatform());
        Assertions.assertEquals(result.getScore(), reviewInput.getScore());
        Assertions.assertEquals(result.getMovieEntity(), movieEntity);
    }

    @Test
    void testAddReviewThrowsException() {
        // given
        var reviewInput = ReviewInput.newBuilder()
                .platform(TestHelper.PLATFORM)
                .movie(TestHelper.MOVIE_NAME)
                .score(TestHelper.SCORE)
                .build();

        Mockito.when(movieService.getMovieByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(NoSuchElementException.class, () -> reviewService.addReview(reviewInput));
    }

    @Test
    void testGetReviewsByMovieAndPlatformMovieIsPresent() {
        // given
        var movieEntity = new MovieEntity(TestHelper.MOVIE_NAME);
        var reviewEntity = new ReviewEntity(TestHelper.SCORE, TestHelper.PLATFORM, movieEntity);

        Mockito.when(movieService.getMovieByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.of(movieEntity));
        Mockito.when(reviewRepository.findByMovieEntityAndPlatform(movieEntity, TestHelper.PLATFORM)).thenReturn(Optional.of(reviewEntity));

        // when
        var reviewEntities = reviewService.getReviewsByMovieAndPlatform(TestHelper.MOVIE_NAME, TestHelper.PLATFORM);

        // then
        Assertions.assertFalse(reviewEntities.isEmpty());
        Assertions.assertEquals(reviewEntities.size(), 1);
        Assertions.assertEquals(reviewEntities.get(0), reviewEntity);
    }

    @Test
    void testGetReviewsByMovieIsPresent() {
        // given
        var movieEntity = new MovieEntity(TestHelper.MOVIE_NAME);
        var reviewEntity = new ReviewEntity(TestHelper.SCORE, TestHelper.PLATFORM, movieEntity);

        Mockito.when(movieService.getMovieByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.of(movieEntity));
        Mockito.when(reviewRepository.findAllByMovieEntity(movieEntity)).thenReturn(List.of(reviewEntity));

        // when
        var reviewEntities = reviewService.getReviewsByMovie(TestHelper.MOVIE_NAME);

        // then
        Assertions.assertFalse(reviewEntities.isEmpty());
        Assertions.assertEquals(reviewEntities.size(), 1);
        Assertions.assertEquals(reviewEntities.get(0), reviewEntity);
    }

    @Test
    void testGetReviewsByMovieIsNotPresent() {
        // given
        Mockito.when(movieService.getMovieByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.empty());

        // when
        var reviewEntities = reviewService.getReviewsByMovie(TestHelper.MOVIE_NAME);

        // then
        Assertions.assertTrue(reviewEntities.isEmpty());
    }


}
