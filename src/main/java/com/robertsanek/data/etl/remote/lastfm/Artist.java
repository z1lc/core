package com.robertsanek.data.etl.remote.lastfm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "last_fm_artists")
public class Artist {

  @Id
  private Long rank;
  private String name;
  @Column(name = "play_count")
  private Long playCount;
  @Column(name = "found_in_anki")
  private boolean foundInAnki;
  @Column(name = "image_url")
  private String imageUrl;

  public Long getRank() {
    return rank;
  }

  public String getName() {
    return name;
  }

  public Long getPlayCount() {
    return playCount;
  }

  public boolean isFoundInAnki() {
    return foundInAnki;
  }

  public String isImageUrl() {
    return imageUrl;
  }

  public static final class ArtistBuilder {

    private Long rank;
    private String name;
    private Long playCount;
    private boolean foundInAnki;
    private String imageUrl;

    private ArtistBuilder() {}

    public static ArtistBuilder anArtist() {
      return new ArtistBuilder();
    }

    public ArtistBuilder withRank(Long rank) {
      this.rank = rank;
      return this;
    }

    public ArtistBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public ArtistBuilder withPlayCount(Long playCount) {
      this.playCount = playCount;
      return this;
    }

    public ArtistBuilder withFoundInAnki(boolean foundInAnki) {
      this.foundInAnki = foundInAnki;
      return this;
    }

    public ArtistBuilder withImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
      return this;
    }

    public Artist build() {
      Artist artist = new Artist();
      artist.imageUrl = this.imageUrl;
      artist.rank = this.rank;
      artist.name = this.name;
      artist.foundInAnki = this.foundInAnki;
      artist.playCount = this.playCount;
      return artist;
    }
  }
}
