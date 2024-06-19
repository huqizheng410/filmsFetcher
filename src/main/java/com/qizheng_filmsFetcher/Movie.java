package com.qizheng_filmsFetcher;

public class Movie {
    // Movie attributes
    private String title;
    private String genre;
    private String releaseDate;
    private String releaseYear;
    private String duration;
    private String language;
    private String country;
    private String director;
    private String rating;
    private String description;
    private String url;

    // Constructor to initialize movie attributes
    public Movie(String title, String genre, String releaseDate, String releaseYear, String duration, String language, String country, String director, String rating, String description, String url) {
        this.title = title != null ? title : "NA";
        this.genre = genre != null ? genre : "NA";
        this.releaseDate = releaseDate != null ? releaseDate : "NA";
        this.releaseYear = releaseYear != null ? releaseYear : "NA";
        this.duration = duration != null ? duration : "NA";
        this.language = language != null ? language : "NA";
        this.country = country != null ? country : "NA";
        this.director = director != null ? director : "NA";
        this.rating = rating != null ? rating : "NA";
        this.description = description != null ? description : "NA";
        this.url = url;
    }

    // Get methods to get detail of attribute
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getReleaseDate() { return releaseDate; }
    public String getReleaseYear() { return releaseYear; }
    public String getDuration() { return duration; }
    public String getLanguage() { return language; }
    public String getCountry() { return country; }
    public String getDirector() { return director; }
    public String getRating() { return rating; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }
}
