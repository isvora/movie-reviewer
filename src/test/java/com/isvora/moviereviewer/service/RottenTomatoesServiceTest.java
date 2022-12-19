package com.isvora.moviereviewer.service;

import com.isvora.moviereviewer.TestHelper;
import com.isvora.moviereviewer.config.RottenTomatoesConfiguration;
import com.isvora.moviereviewer.services.RottenTomatoesService;
import com.isvora.moviereviewer.type.Platform;
import com.isvora.moviereviewer.type.Source;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RottenTomatoesServiceTest {

    @Mock
    private RottenTomatoesConfiguration rottenTomatoesConfiguration;

    private RottenTomatoesService rottenTomatoesService;

    @BeforeEach
    void init() {
        rottenTomatoesService = new RottenTomatoesService(rottenTomatoesConfiguration);
        Mockito.when(rottenTomatoesConfiguration.getUrl()).thenReturn("https://www.rottentomatoes.com/m/%s");
    }

    @Test
    void testSearchCriticRating() {
        var rating = rottenTomatoesService.searchCriticRating(TestHelper.MOVIE_NAME);

        Assertions.assertNotNull(rating);
        Assertions.assertEquals(rating.getMovie(), TestHelper.MOVIE_NAME);
        Assertions.assertEquals(rating.getPlatform(), Platform.ROTTEN_TOMATOES);
        Assertions.assertEquals(rating.getScore(), 97);
        Assertions.assertEquals(rating.getSource(), Source.CRITIC);
    }

    @Test
    void testSearchAudienceRating() {
        var rating = rottenTomatoesService.searchAudienceRating(TestHelper.MOVIE_NAME);

        Assertions.assertNotNull(rating);
        Assertions.assertEquals(rating.getMovie(), TestHelper.MOVIE_NAME);
        Assertions.assertEquals(rating.getPlatform(), Platform.ROTTEN_TOMATOES);
        Assertions.assertEquals(rating.getScore(), 96);
        Assertions.assertEquals(rating.getSource(), Source.AUDIENCE);
    }
}
