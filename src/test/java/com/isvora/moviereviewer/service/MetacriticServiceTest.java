package com.isvora.moviereviewer.service;

import com.isvora.moviereviewer.TestHelper;
import com.isvora.moviereviewer.config.MetacriticConfiguration;
import com.isvora.moviereviewer.services.MetacriticService;
import com.isvora.moviereviewer.type.Platform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MetacriticServiceTest {

    @Mock
    private MetacriticConfiguration metacriticConfiguration;

    private MetacriticService metacriticService;

    @BeforeEach
    void init() {
        metacriticService = new MetacriticService(metacriticConfiguration);
    }

    @Test
    void testSearchRating() {
        // given
        Mockito.when(metacriticConfiguration.getUrl()).thenReturn("https://www.metacritic.com/movie/%s");

        // when
        var rating = metacriticService.searchRating(TestHelper.MOVIE_NAME);

        // then
        Assertions.assertNotNull(rating);
        Assertions.assertEquals(rating.getPlatform(), Platform.METACRITIC);
        Assertions.assertEquals(rating.getScore(), TestHelper.SCORE_2);
        Assertions.assertEquals(rating.getMovie(), TestHelper.MOVIE_NAME);
    }
}
