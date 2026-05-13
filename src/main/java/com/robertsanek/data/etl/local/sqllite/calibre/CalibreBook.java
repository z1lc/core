package com.robertsanek.data.etl.local.sqllite.calibre;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "calibre_books")
public class CalibreBook {

  @Id
  private Long id;
  private String title;

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public static final class BookBuilder {

    private Long id;
    private String title;

    private BookBuilder() {}

    public static BookBuilder aBook() {
      return new BookBuilder();
    }

    public BookBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public BookBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    public CalibreBook build() {
      CalibreBook calibreBook = new CalibreBook();
      calibreBook.title = this.title;
      calibreBook.id = this.id;
      return calibreBook;
    }
  }
}
