package com.qizheng_filmsFetcher;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ExcelHelperTest {
    @Test
    public void testWriteMoviesToExcel() {
        List<Movie> movies = Arrays.asList(
                new Movie("Title1", "Genre1", "2021-01-01", "2021", "120", "English", "USA", "Director1", "5", "Description1", "url1"),
                new Movie("Title2", "Genre2", "2021-02-02", "2021", "130", "French", "France", "Director2", "4", "Description2", "url2")
        );

        assertDoesNotThrow(() -> ExcelHelper.writeMoviesToExcel(movies, "test_movies.xlsx"));
        new File("test_movies.xlsx").delete(); // Clean up files
    }
}
