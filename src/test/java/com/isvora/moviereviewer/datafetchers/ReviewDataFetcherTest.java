package com.isvora.moviereviewer.datafetchers;

import com.google.common.io.Resources;
import com.isvora.moviereviewer.TestHelper;
import com.isvora.moviereviewer.database.MovieEntity;
import com.isvora.moviereviewer.database.ReviewEntity;
import com.isvora.moviereviewer.model.Rating;
import com.isvora.moviereviewer.services.ImdbService;
import com.isvora.moviereviewer.services.MovieService;
import com.isvora.moviereviewer.services.ReviewService;
import com.isvora.moviereviewer.type.Platform;
import com.isvora.moviereviewer.type.Source;
import com.jayway.jsonpath.TypeRef;
import com.netflix.dgs.codegen.generated.types.*;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
class ReviewDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private MovieService movieService;

    @MockBean
    private ImdbService imdbService;

    @Test
    void testGetAllReviewsForMovieFromPlatform() throws IOException {
        List<ReviewEntity> reviewEntities = new ArrayList<>();
        MovieEntity movieEntity = new MovieEntity(TestHelper.MOVIE_NAME);
        reviewEntities.add(new ReviewEntity(TestHelper.SCORE, TestHelper.IMDB, TestHelper.SOURCE, movieEntity));
        Mockito.when(reviewService.getReviewsByMovieAndPlatform(TestHelper.MOVIE_NAME, TestHelper.IMDB)).thenReturn(reviewEntities);

        Map<String, Object> map = new HashMap<>() {{
            put("movie", TestHelper.MOVIE_NAME);
            put ("platform", TestHelper.IMDB);
        }};

        URL url = Resources.getResource("graphql/getAllReviewsFromMovieForPlatform.graphql");
        @Language("GraphQL") String query = Resources.toString(url, StandardCharsets.UTF_8);

        List<ReviewResponse> reviewResponses = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query,
                "data.reviews",
                map,
                new TypeRef<>() {
                }
        );

        Assertions.assertFalse(reviewResponses.isEmpty());
        Assertions.assertEquals(reviewResponses.size(), 1);
        Assertions.assertEquals(reviewResponses.get(0).getPlatform(), TestHelper.IMDB);
        Assertions.assertEquals(reviewResponses.get(0).getScore(), TestHelper.SCORE);
    }

    @Test
    void testGetAllReviewsForMovie() throws IOException {
        List<ReviewEntity> reviewEntities = new ArrayList<>();
        MovieEntity movieEntity = new MovieEntity(TestHelper.MOVIE_NAME);
        reviewEntities.add(new ReviewEntity(TestHelper.SCORE, TestHelper.IMDB, TestHelper.SOURCE, movieEntity));
        Mockito.when(reviewService.getReviewsByMovieAndPlatform(TestHelper.MOVIE_NAME, null)).thenReturn(reviewEntities);

        Map<String, Object> map = new HashMap<>() {{
            put("movie", TestHelper.MOVIE_NAME);
        }};

        URL url = Resources.getResource("graphql/getAllReviewsForMovie.graphql");
        @Language("GraphQL") String query = Resources.toString(url, StandardCharsets.UTF_8);

        List<ReviewResponse> reviewResponses = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query,
                "data.reviews",
                map,
                new TypeRef<>() {
                }
        );

        Assertions.assertFalse(reviewResponses.isEmpty());
        Assertions.assertEquals(reviewResponses.size(), 1);
        Assertions.assertEquals(reviewResponses.get(0).getPlatform(), TestHelper.IMDB);
        Assertions.assertEquals(reviewResponses.get(0).getScore(), TestHelper.SCORE);
    }

    @Test
    void testAddReview() throws IOException {
        MovieEntity movieEntity = new MovieEntity(TestHelper.MOVIE_NAME);
        ReviewEntity reviewEntity = new ReviewEntity(TestHelper.SCORE, TestHelper.IMDB, TestHelper.SOURCE, movieEntity);

        Mockito.when(movieService.getMovieByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.of(movieEntity));
        Mockito.when(reviewService.addReview(any(ReviewInput.class), eq(null))).thenReturn(reviewEntity);

        Map<String, Object> map = new HashMap<>() {{
            put("movie", TestHelper.MOVIE_NAME);
            put("score", TestHelper.SCORE);
            put("platform", TestHelper.IMDB);
        }};

        URL url = Resources.getResource("graphql/addReview.graphql");
        @Language("GraphQL") String query = Resources.toString(url, StandardCharsets.UTF_8);

        ReviewResponse reviewResponses = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query,
                "data.addReview",
                map,
                new TypeRef<>() {
                }
        );

        Assertions.assertNotNull(reviewResponses);
        Assertions.assertEquals(reviewResponses.getScore(), TestHelper.SCORE);
        Assertions.assertEquals(reviewResponses.getPlatform(), TestHelper.IMDB);
    }

    @Test
    void testAddReviewMovieNotFound() throws IOException {
        Mockito.when(movieService.getMovieByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.empty());

        Map<String, Object> map = new HashMap<>() {{
            put("movie", TestHelper.MOVIE_NAME);
            put("score", TestHelper.SCORE);
            put("platform", TestHelper.IMDB);
        }};

        URL url = Resources.getResource("graphql/addReview.graphql");
        @Language("GraphQL") String query = Resources.toString(url, StandardCharsets.UTF_8);

        ReviewError reviewResponses = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query,
                "data.addReview",
                map,
                new TypeRef<>() {
                }
        );

        Assertions.assertNotNull(reviewResponses);
        Assertions.assertEquals(reviewResponses.getErrors().get(0), ReviewErrorEnum.MOVIE_NOT_FOUND);
    }

    @Test
    void testScrapeRating() throws IOException {
        MovieEntity movieEntity = new MovieEntity(TestHelper.MOVIE_NAME);
        ReviewEntity reviewEntity = new ReviewEntity(TestHelper.SCORE_2, TestHelper.METACRITIC, TestHelper.SOURCE, movieEntity);
        movieEntity.setReviewEntities(Set.of(reviewEntity));

        Mockito.when(movieService.getMovieByName(TestHelper.MOVIE_NAME)).thenReturn(Optional.of(movieEntity));
        Mockito.when(imdbService.searchRating(TestHelper.MOVIE_NAME)).thenReturn(
                new Rating(TestHelper.SCORE, Platform.IMDB, TestHelper.MOVIE_NAME,
                        Source.AUDIENCE));
        Mockito.when(reviewService.addReview(any(ReviewInput.class), anyString())).thenReturn(reviewEntity);

        Map<String, Object> map = new HashMap<>() {{
            put("movie", TestHelper.MOVIE_NAME);
        }};

        URL url = Resources.getResource("graphql/scrapeRatings.graphql");
        @Language("GraphQL") String query = Resources.toString(url, StandardCharsets.UTF_8);

        List<ReviewResponse> reviewResponses = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query,
                "data.scrapeRatings",
                map,
                new TypeRef<>() {
                }
        );

        Assertions.assertFalse(reviewResponses.isEmpty());
        Assertions.assertEquals(reviewResponses.get(0).getPlatform(), TestHelper.METACRITIC);
        Assertions.assertEquals(reviewResponses.get(0).getScore(), TestHelper.SCORE_2);
    }
}
