package com.robertsanek.data.etl.remote.scrape.indiehackers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "indie_hackers_books")
public class IndieHackersBook {

  @Id
  private Long id;
  @Column(name = "anchor_text")
  private String anchorText;
  private String url;
  @Column(name = "indie_hackers_url")
  private String indieHackersUrl;
  @Column(name = "indie_hackers_kudos")
  private Long indieHackersKudos;
  @Column(name = "indie_hackers_title")
  private String indieHackersTitle;

  public static final class IndieHackersBookBuilder {
    private Long id;
    private String anchorText;
    private String url;
    private String indieHackersUrl;
    private Long indieHackersKudos;
    private String indieHackersTitle;

    private IndieHackersBookBuilder() {
    }

    public static IndieHackersBookBuilder anIndieHackersBook() {
      return new IndieHackersBookBuilder();
    }

    public IndieHackersBookBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public IndieHackersBookBuilder withAnchorText(String anchorText) {
      this.anchorText = anchorText;
      return this;
    }

    public IndieHackersBookBuilder withUrl(String url) {
      this.url = url;
      return this;
    }

    public IndieHackersBookBuilder withIndieHackersUrl(String indieHackersUrl) {
      this.indieHackersUrl = indieHackersUrl;
      return this;
    }

    public IndieHackersBookBuilder withIndieHackersKudos(Long indieHackersKudos) {
      this.indieHackersKudos = indieHackersKudos;
      return this;
    }

    public IndieHackersBookBuilder withIndieHackersTitle(String indieHackersTitle) {
      this.indieHackersTitle = indieHackersTitle;
      return this;
    }

    public IndieHackersBook build() {
      IndieHackersBook indieHackersBook = new IndieHackersBook();
      indieHackersBook.indieHackersTitle = this.indieHackersTitle;
      indieHackersBook.indieHackersKudos = this.indieHackersKudos;
      indieHackersBook.id = this.id;
      indieHackersBook.anchorText = this.anchorText;
      indieHackersBook.indieHackersUrl = this.indieHackersUrl;
      indieHackersBook.url = this.url;
      return indieHackersBook;
    }
  }
}
