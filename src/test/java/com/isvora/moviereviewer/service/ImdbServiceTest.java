package com.isvora.moviereviewer.service;

import com.isvora.moviereviewer.TestHelper;
import com.isvora.moviereviewer.services.ImdbService;
import com.isvora.moviereviewer.type.Platform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImdbServiceTest {

    private ImdbService imdbService;

    @BeforeEach
    void init() {
        imdbService = new ImdbService();
    }

    @Test
    void testSearchRating() {
        // when
        var rating = imdbService.searchRating(TestHelper.MOVIE_NAME);

        // then
        Assertions.assertNotNull(rating);
        Assertions.assertEquals(rating.getMovie(), TestHelper.MOVIE_NAME);
        Assertions.assertEquals(rating.getScore(), TestHelper.SCORE);
        Assertions.assertEquals(rating.getPlatform(), Platform.IMDB);
    }
}
