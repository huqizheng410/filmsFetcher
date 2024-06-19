package com.qizheng_filmsFetcher;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class WebScraperTest {

    @Test
    public void testFetchMovieDetails() throws Exception {
        Method method = WebScraper.class.getDeclaredMethod("fetchMovieDetails", String.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(new WebScraper(), "https://elcinema.com/en/work/1234567"));
    }
}
