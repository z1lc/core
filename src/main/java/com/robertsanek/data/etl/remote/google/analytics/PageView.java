package com.robertsanek.data.etl.remote.google.analytics;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "google_analytics_page_views")
public class PageView {

  @Id
  private long id;
  private LocalDate date;
  private long pageViews;
  private String website;

  public static final class PageViewBuilder {

    private long id;
    private LocalDate date;
    private long pageViews;
    private String website;

    private PageViewBuilder() {}

    public static PageViewBuilder aPageView() {
      return new PageViewBuilder();
    }

    public PageViewBuilder withId(long id) {
      this.id = id;
      return this;
    }

    public PageViewBuilder withDate(LocalDate date) {
      this.date = date;
      return this;
    }

    public PageViewBuilder withPageViews(long pageViews) {
      this.pageViews = pageViews;
      return this;
    }

    public PageViewBuilder withWebsite(String website) {
      this.website = website;
      return this;
    }

    public PageView build() {
      PageView pageView = new PageView();
      pageView.id = this.id;
      pageView.date = this.date;
      pageView.pageViews = this.pageViews;
      pageView.website = this.website;
      return pageView;
    }
  }
}
