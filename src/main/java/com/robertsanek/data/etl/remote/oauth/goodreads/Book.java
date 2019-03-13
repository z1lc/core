package com.robertsanek.data.etl.remote.oauth.goodreads;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "goodreads_books")
public class Book {

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

  public static final class BookBuilder {

    private Long id;
    private String isbn13;
    private String title;
    private String authorName;
    private String authorImageUrl;
    private Long yearPublished;
    private ZonedDateTime addedOn;
    private boolean foundInAnki;

    private BookBuilder() {}

    public static BookBuilder aBook() {
      return new BookBuilder();
    }

    public BookBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public BookBuilder withIsbn13(String isbn13) {
      this.isbn13 = isbn13;
      return this;
    }

    public BookBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    public BookBuilder withAuthorName(String authorName) {
      this.authorName = authorName;
      return this;
    }

    public BookBuilder withAuthorImageUrl(String authorImageUrl) {
      this.authorImageUrl = authorImageUrl;
      return this;
    }

    public BookBuilder withYearPublished(Long yearPublished) {
      this.yearPublished = yearPublished;
      return this;
    }

    public BookBuilder withAddedOn(ZonedDateTime addedOn) {
      this.addedOn = addedOn;
      return this;
    }

    public BookBuilder withFoundInAnki(boolean foundInAnki) {
      this.foundInAnki = foundInAnki;
      return this;
    }

    public Book build() {
      Book book = new Book();
      book.isbn13 = this.isbn13;
      book.authorName = this.authorName;
      book.foundInAnki = this.foundInAnki;
      book.id = this.id;
      book.authorImageUrl = this.authorImageUrl;
      book.title = this.title;
      book.addedOn = this.addedOn;
      book.yearPublished = this.yearPublished;
      return book;
    }
  }
}
