package com.robertsanek.data.etl.local.sqllite.calibre;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "calibre_incremental_reading_priorities")
public class IncrementalReadingPriority {

  @Id
  private Long id;
  @Column(name = "book_id")
  private Long bookId;
  private Long value;

  public Long getId() {
    return id;
  }

  public Long getBookId() {
    return bookId;
  }

  public Long getValue() {
    return value;
  }

  public static final class IncrementalReadingPriorityBuilder {

    private Long id;
    private Long bookId;
    private Long value;

    private IncrementalReadingPriorityBuilder() {}

    public static IncrementalReadingPriorityBuilder anIncrementalReadingPriority() {
      return new IncrementalReadingPriorityBuilder();
    }

    public IncrementalReadingPriorityBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public IncrementalReadingPriorityBuilder withBookId(Long bookId) {
      this.bookId = bookId;
      return this;
    }

    public IncrementalReadingPriorityBuilder withValue(Long value) {
      this.value = value;
      return this;
    }

    public IncrementalReadingPriority build() {
      IncrementalReadingPriority incrementalReadingPriority = new IncrementalReadingPriority();
      incrementalReadingPriority.bookId = this.bookId;
      incrementalReadingPriority.value = this.value;
      incrementalReadingPriority.id = this.id;
      return incrementalReadingPriority;
    }
  }
}
