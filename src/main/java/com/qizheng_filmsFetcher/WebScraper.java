package com.qizheng_filmsFetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraper {
    private static final Logger logger = LogManager.getLogger(WebScraper.class);

    // Method to scrape a page by index
    public static List<Movie> scrapePage(String baseUrl, int pageIndex) throws IOException {
        String url = baseUrl + pageIndex;
        return scrapePage(url);
    }

    // Method to scrape a page by URL
    public static List<Movie> scrapePage(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements rows = doc.select("table.expand tr");
        List<Movie> movies = new ArrayList<>();

        // Parsing the table rows for movie details
        for (Element row : rows) {
            Elements cells = row.select("td");
            if (cells.size() > 0) {
                String title = cells.get(1).select("a").text();
                String movieUrl = "https://elcinema.com" + cells.get(1).select("a").attr("href");

                // Fetch detailed information from the movie's page
                Movie movie = fetchMovieDetails(movieUrl);
                if (movie != null) {
                    movies.add(movie);
                }
            }
        }
        logger.info("Page " + url + " scraped successfully.");
        return movies;
    }

    // Method to fetch detailed movie information from its page
    protected static Movie fetchMovieDetails(String movieUrl) {
        try {
            Document doc = Jsoup.connect(movieUrl).get();

            String title = getText(doc.select("h1 span.left[dir=ltr]").first());
            String genre = getText(doc.select("ul.list-separator li a[href*=genre]").first());
            String releaseDate = getText(doc.select("ul.list-separator li a[href*=release_day]").first());
            String releaseYear = getText(doc.select("ul.list-separator.inline li a[href*=release_year]").first());
            String duration = getText(doc.select("div.columns.small-9.large-10 ul.list-separator li:contains(minutes)").first());
            String language = getText(doc.select("ul.list-separator li a[href*=language]").first());
            String country = getText(doc.select("ul.list-separator li a[href*=country]").first());
            String director = getText(doc.select("ul.list-separator.list-title:contains(Director) li a").first());
            String rating = getText(doc.select("div.stars-rating-lg span.legend").first());
            String description = getText(doc.select("p").first());

            return new Movie(title, genre, releaseDate, releaseYear, duration, language, country, director, rating, description, movieUrl);
        } catch (IOException e) {
            logger.error("Failed to fetch details for URL: " + movieUrl);
            return null;
        }
    }

    // Method to get text content from an element
    private static String getText(Element element) {
        return element != null ? element.text() : "NA";
    }
}
