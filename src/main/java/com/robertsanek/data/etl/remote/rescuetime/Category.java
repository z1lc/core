package com.robertsanek.data.etl.remote.rescuetime;

import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rescuetime_daily_categories")
public class Category {

  @Id
  public Integer id;
  public ZonedDateTime date;
  @Column(name = "time_spent_seconds")
  public Long timeSpentSeconds;
  public String category;

  public Category() { }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Category category1 = (Category) o;
    return Objects.equals(id, category1.id) &&
        Objects.equals(date, category1.date) &&
        Objects.equals(timeSpentSeconds, category1.timeSpentSeconds) &&
        Objects.equals(category, category1.category);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, date, timeSpentSeconds, category);
  }

  public static final class CategoryBuilder {

    public Integer id;
    public ZonedDateTime date;
    public Long timeSpentSeconds;
    public String category;

    private CategoryBuilder() {}

    public static CategoryBuilder aCategory() { return new CategoryBuilder(); }

    public CategoryBuilder withId(Integer id) {
      this.id = id;
      return this;
    }

    public CategoryBuilder withDate(ZonedDateTime date) {
      this.date = date;
      return this;
    }

    public CategoryBuilder withTimeSpentSeconds(Long timeSpentSeconds) {
      this.timeSpentSeconds = timeSpentSeconds;
      return this;
    }

    public CategoryBuilder withCategory(String category) {
      this.category = category;
      return this;
    }

    public Category build() {
      Category category = new Category();
      category.timeSpentSeconds = this.timeSpentSeconds;
      category.category = this.category;
      category.date = this.date;
      category.id = this.id;
      return category;
    }
  }
}
