package com.robertsanek.data.etl.remote.scrape.rottentomatoes;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rotten_tomatoes_movies")
public class Movie {

  @Id
  private Long id;
  private String title;
  private String url;
  private Long rating;
  private Long year;

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getUrl() {
    return url;
  }

  public Long getRating() {
    return rating;
  }

  public Long getYear() {
    return year;
  }

  public static final class MovieBuilder {

    private Long id;
    private String title;
    private String url;
    private Long rating;
    private Long year;

    private MovieBuilder() {}

    public static MovieBuilder aMovie() {
      return new MovieBuilder();
    }

    public MovieBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public MovieBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    public MovieBuilder withUrl(String url) {
      this.url = url;
      return this;
    }

    public MovieBuilder withRating(Long rating) {
      this.rating = rating;
      return this;
    }

    public MovieBuilder withYear(Long year) {
      this.year = year;
      return this;
    }

    public Movie build() {
      Movie movie = new Movie();
      movie.url = this.url;
      movie.rating = this.rating;
      movie.year = this.year;
      movie.id = this.id;
      movie.title = this.title;
      return movie;
    }
  }
}
