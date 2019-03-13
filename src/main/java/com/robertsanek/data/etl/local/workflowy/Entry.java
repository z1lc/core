package com.robertsanek.data.etl.local.workflowy;

import static com.robertsanek.data.etl.local.workflowy.WorkflowyEtl.TEXT_LIMIT;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "workflowy_entries")
public class Entry {

  @Id
  private Long id;
  private Long parent_id;
  @Column(length = TEXT_LIMIT)
  private String text;
  @Column(length = TEXT_LIMIT)
  private String note;
  private Long num_children;
  private ZonedDateTime date_exported;

  public static final class EntryBuilder {

    private Long id;
    private Long parent_id;
    private String text;
    private String note;
    private Long num_children;
    private ZonedDateTime date_exported;

    private EntryBuilder() {}

    public static EntryBuilder anEntry() {
      return new EntryBuilder();
    }

    public EntryBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public EntryBuilder withParent_id(Long parent_id) {
      this.parent_id = parent_id;
      return this;
    }

    public EntryBuilder withText(String text) {
      this.text = text;
      return this;
    }

    public EntryBuilder withNote(String note) {
      this.note = note;
      return this;
    }

    public EntryBuilder withNum_children(Long num_children) {
      this.num_children = num_children;
      return this;
    }

    public EntryBuilder withDate_exported(ZonedDateTime date_exported) {
      this.date_exported = date_exported;
      return this;
    }

    public Entry build() {
      Entry entry = new Entry();
      entry.id = this.id;
      entry.note = this.note;
      entry.parent_id = this.parent_id;
      entry.num_children = this.num_children;
      entry.text = this.text;
      entry.date_exported = this.date_exported;
      return entry;
    }
  }
}
