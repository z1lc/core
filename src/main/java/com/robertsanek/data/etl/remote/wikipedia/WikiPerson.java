package com.robertsanek.data.etl.remote.wikipedia;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.robertsanek.wikipedia.WikiArticle;

@Entity
@Table(name = "wikipedia_people")
public class WikiPerson {

  @Id
  private Long rank;
  private Long hits_in_past_year;
  private String wikipedia_url_title;
  private boolean found_in_anki;
  private LocalDate birth_day;
  private LocalDate death_day;
  private String image_url;

  public WikiArticle toArticle() {
    return new WikiArticle(this.wikipedia_url_title, this.hits_in_past_year, this.rank);
  }

  public Long getRank() {
    return rank;
  }

  public Long getHits_in_past_year() {
    return hits_in_past_year;
  }

  public String getWikipedia_url_title() {
    return wikipedia_url_title;
  }

  public boolean isFound_in_anki() {
    return found_in_anki;
  }

  public LocalDate getBirth_day() {
    return birth_day;
  }

  public LocalDate getDeath_day() {
    return death_day;
  }

  public String getImage_url() {
    return image_url;
  }

  public static final class WikiPersonBuilder {

    private Long rank;
    private Long hits_in_past_year;
    private String wikipedia_url_title;
    private boolean found_in_anki;
    private LocalDate birth_day;
    private LocalDate death_day;
    private String image_url;

    private WikiPersonBuilder() {}

    public static WikiPersonBuilder aWikiPerson() {
      return new WikiPersonBuilder();
    }

    public WikiPersonBuilder withRank(Long rank) {
      this.rank = rank;
      return this;
    }

    public WikiPersonBuilder withHits_in_past_year(Long hits_in_past_year) {
      this.hits_in_past_year = hits_in_past_year;
      return this;
    }

    public WikiPersonBuilder withWikipedia_url_title(String wikipedia_url_title) {
      this.wikipedia_url_title = wikipedia_url_title;
      return this;
    }

    public WikiPersonBuilder withFound_in_anki(boolean found_in_anki) {
      this.found_in_anki = found_in_anki;
      return this;
    }

    public WikiPersonBuilder withBirth_day(LocalDate birth_day) {
      this.birth_day = birth_day;
      return this;
    }

    public WikiPersonBuilder withDeath_day(LocalDate death_day) {
      this.death_day = death_day;
      return this;
    }

    public WikiPersonBuilder withImage_url(String image_url) {
      this.image_url = image_url;
      return this;
    }

    public WikiPerson build() {
      WikiPerson wikiPerson = new WikiPerson();
      wikiPerson.hits_in_past_year = this.hits_in_past_year;
      wikiPerson.birth_day = this.birth_day;
      wikiPerson.death_day = this.death_day;
      wikiPerson.image_url = this.image_url;
      wikiPerson.rank = this.rank;
      wikiPerson.wikipedia_url_title = this.wikipedia_url_title;
      wikiPerson.found_in_anki = this.found_in_anki;
      return wikiPerson;
    }
  }
}
