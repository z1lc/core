package com.robertsanek.wikipedia;

import javax.annotation.Nonnull;

public class WikiArticle implements Comparable<WikiArticle> {

  private String urlTitle;
  private Long hits;
  private Long rank;

  public WikiArticle(String urlTitle, Long hits, Long rank) {
    this.urlTitle = urlTitle;
    this.hits = hits;
    this.rank = rank;
  }

  public String getUrlTitle() {
    return urlTitle;
  }

  public Long getHits() {
    return hits;
  }

  public String getPrettyTitle() {
    return urlTitle.replace("_", " ");
  }

  public String getURL() {
    return String.format("https://en.wikipedia.org/wiki/%s", urlTitle);
  }

  public Long getRank() {
    return rank;
  }

  @Override
  public int compareTo(@Nonnull WikiArticle o) {
    return this.getRank().compareTo(o.getRank());
  }
}
