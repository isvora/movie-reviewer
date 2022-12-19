package com.isvora.moviereviewer.datafetchers;

import com.google.common.io.Resources;
import com.isvora.moviereviewer.TestHelper;
import com.isvora.moviereviewer.database.MovieEntity;
import com.isvora.moviereviewer.services.MovieService;
import com.jayway.jsonpath.TypeRef;
import com.netflix.dgs.codegen.generated.types.Movie;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class MovieDataFetcherTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @MockBean
    private MovieService movieService;

    @Test
    void testGetAllMovies() throws IOException {
        List<MovieEntity> movieEntities = new ArrayList<>();
        movieEntities.add(new MovieEntity(TestHelper.MOVIE_NAME));
        Mockito.when(movieService.getMovies(null)).thenReturn(movieEntities);

        URL url = Resources.getResource("graphql/getAllMovies.graphql");
        @Language("GraphQL") String query = Resources.toString(url, StandardCharsets.UTF_8);

        List<Movie> movies = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query,
                "data.movies",
                new TypeRef<>() {
                }
        );

        Assertions.assertFalse(movies.isEmpty());
        Assertions.assertEquals(movies.size(), 1);
        Assertions.assertEquals(movies.get(0).getName(), TestHelper.MOVIE_NAME);
    }

    @Test
    void testGetSpecificMovie() throws IOException {
        List<MovieEntity> movieEntities = new ArrayList<>();
        movieEntities.add(new MovieEntity(TestHelper.MOVIE_NAME));
        Mockito.when(movieService.getMovies(TestHelper.MOVIE_NAME)).thenReturn(movieEntities);

        Map<String, Object> map = new HashMap<>() {{
            put("movie", TestHelper.MOVIE_NAME);
        }};

        URL url = Resources.getResource("graphql/getMovie.graphql");
        @Language("GraphQL") String query = Resources.toString(url, StandardCharsets.UTF_8);

        List<Movie> movies = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                query,
                "data.movies",
                map,
                new TypeRef<>() {
                }
        );

        Assertions.assertFalse(movies.isEmpty());
        Assertions.assertEquals(movies.size(), 1);
        Assertions.assertEquals(movies.get(0).getName(), TestHelper.MOVIE_NAME);
    }
}
