package com.qizheng_filmsFetcher;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class MovieScraperTest {

    @Test
    public void testCollectData() throws Exception {
        Future<List<Movie>> futureMock = mock(Future.class);
        try {
            when(futureMock.get()).thenReturn(Arrays.asList(
                    new Movie("Title1", "Genre1", "2021-01-01", "2021", "120", "English", "USA", "Director1", "5", "Description1", "url1"),
                    new Movie("Title2", "Genre2", "2021-02-02", "2021", "130", "French", "France", "Director2", "4", "Description2", "url2")
            ));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        List<Future<List<Movie>>> futures = Arrays.asList(futureMock);
        Method method = MovieScraper.class.getDeclaredMethod("collectData", List.class, String.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(new MovieScraper(), futures, "movie"));
    }
}
