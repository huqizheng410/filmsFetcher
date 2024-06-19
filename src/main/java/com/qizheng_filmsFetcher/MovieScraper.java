package com.qizheng_filmsFetcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MovieScraper {
    private static final Logger logger = LogManager.getLogger(MovieScraper.class);
    private static final String BASE_URL_MOVIE = "https://elcinema.com/en/index/work/category/1?page=";
    private static final String BASE_URL_SERIES = "https://elcinema.com/en/index/work/category/3?page=";
    private static final int TOTAL_PAGES = 10; // Set to be 10 just for this demo.

    public static void main(String[] args) {
        List<Future<List<Movie>>> movieFutures = new ArrayList<>();
        List<Future<List<Movie>>> seriesFutures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // Submitting tasks to scrape movie and series pages concurrently
        for (int i = 1; i <= TOTAL_PAGES; i++) {
            int pageIndex = i;
            movieFutures.add(executorService.submit(() -> WebScraper.scrapePage(BASE_URL_MOVIE, pageIndex)));
            seriesFutures.add(executorService.submit(() -> WebScraper.scrapePage(BASE_URL_SERIES, pageIndex)));
        }

        // Collecting scraped data
        List<Movie> movies = collectData(movieFutures, "movie");
        List<Movie> series = collectData(seriesFutures, "series");

        try {
            // Inserting movies and series data into the database
            DatabaseHelper.batchInsertMovies(movies, "movies");
            DatabaseHelper.batchInsertMovies(series, "tv_shows");

            // Writing data to DynamoDB
            DynamoDBHelper.batchWriteDataToDynamoDB(movies);
            DynamoDBHelper.batchWriteDataToDynamoDB(series);

        } catch (Exception e) {
            List<Movie> unusedMovies = new ArrayList<>(movies);
            List<Movie> unusedSeries = new ArrayList<>(series);
            logger.error("Error while inserting movies into database", e);
            try {
                // Writing data to Excel if database insertion fails
                ExcelHelper.writeMoviesToExcel(unusedMovies, "MoviesDetails.xlsx");
                ExcelHelper.writeMoviesToExcel(unusedSeries, "TVSeriesDetails.xlsx");
            } catch (Exception e1) {
                logger.error("Error while writing movies and series data to Excel", e1);
            }
        }

        // Shutting down the executor service
        executorService.shutdown();
    }

    // Method to collect data from futures
    protected static List<Movie> collectData(List<Future<List<Movie>>> futures, String type) {
        List<Movie> movies = new ArrayList<>();
        for (Future<List<Movie>> future : futures) {
            try {
                movies.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error while fetching " + type + " page data", e);
            }
        }
        return movies;
    }
}
