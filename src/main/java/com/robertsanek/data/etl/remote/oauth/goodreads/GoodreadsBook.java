package com.robertsanek.data.etl.remote.oauth.goodreads;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "goodreads_books")
public class GoodreadsBook {

  @Id
  private Long id;
  private String isbn13;
  private String title;
  @Column(name = "author_name")
  private String authorName;
  @Column(name = "author_image_url")
  private String authorImageUrl;
  @Column(name = "year_published")
  private Long yearPublished;
  @Column(name = "added_on")
  private ZonedDateTime addedOn;
  @Column(name = "found_in_anki")
  private boolean foundInAnki;
  @Column(name = "read")
  private boolean read;

  public Long getId() {
    return id;
  }

  public String getIsbn13() {
    return isbn13;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthorName() {
    return authorName;
  }

  public String getAuthorImageUrl() {
    return authorImageUrl;
  }

  public Long getYearPublished() {
    return yearPublished;
  }

  public ZonedDateTime getAddedOn() {
    return addedOn;
  }

  public boolean isFoundInAnki() {
    return foundInAnki;
  }

  public boolean isRead() {
    return read;
  }

  public static final class GoodreadsBookBuilder {

    private Long id;
    private String isbn13;
    private String title;
    private String authorName;
    private String authorImageUrl;
    private Long yearPublished;
    private ZonedDateTime addedOn;
    private boolean foundInAnki;
    private boolean read;

    private GoodreadsBookBuilder() {}

    public static GoodreadsBookBuilder aGoodreadsBook() {
      return new GoodreadsBookBuilder();
    }

    public GoodreadsBookBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public GoodreadsBookBuilder withIsbn13(String isbn13) {
      this.isbn13 = isbn13;
      return this;
    }

    public GoodreadsBookBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    public GoodreadsBookBuilder withAuthorName(String authorName) {
      this.authorName = authorName;
      return this;
    }

    public GoodreadsBookBuilder withAuthorImageUrl(String authorImageUrl) {
      this.authorImageUrl = authorImageUrl;
      return this;
    }

    public GoodreadsBookBuilder withYearPublished(Long yearPublished) {
      this.yearPublished = yearPublished;
      return this;
    }

    public GoodreadsBookBuilder withAddedOn(ZonedDateTime addedOn) {
      this.addedOn = addedOn;
      return this;
    }

    public GoodreadsBookBuilder withFoundInAnki(boolean foundInAnki) {
      this.foundInAnki = foundInAnki;
      return this;
    }

    public GoodreadsBookBuilder withRead(boolean read) {
      this.read = read;
      return this;
    }

    public GoodreadsBook build() {
      GoodreadsBook goodreadsBook = new GoodreadsBook();
      goodreadsBook.id = this.id;
      goodreadsBook.yearPublished = this.yearPublished;
      goodreadsBook.addedOn = this.addedOn;
      goodreadsBook.title = this.title;
      goodreadsBook.foundInAnki = this.foundInAnki;
      goodreadsBook.isbn13 = this.isbn13;
      goodreadsBook.read = this.read;
      goodreadsBook.authorImageUrl = this.authorImageUrl;
      goodreadsBook.authorName = this.authorName;
      return goodreadsBook;
    }
  }
}
