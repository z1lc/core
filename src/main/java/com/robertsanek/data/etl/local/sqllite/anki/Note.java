package com.robertsanek.data.etl.local.sqllite.anki;

import static com.robertsanek.data.etl.local.sqllite.anki.AnkiEtl.FIELDS_LIMIT;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anki_notes")
public class Note {

  @Id
  Long id;
  ZonedDateTime created_at;
  Long model_id;
  ZonedDateTime modified_at;
  @Column(length = FIELDS_LIMIT)
  String tags;
  @Column(length = FIELDS_LIMIT)
  String fields;
  @Column(name = "sort_field", length = FIELDS_LIMIT)
  String sortField;

  public Long getId() {
    return id;
  }

  public ZonedDateTime getCreated_at() {
    return created_at;
  }

  public Long getModel_id() {
    return model_id;
  }

  public ZonedDateTime getModified_at() {
    return modified_at;
  }

  public String getTags() {
    return tags;
  }

  public String getFields() {
    return fields;
  }

  public String getSortField() {
    return sortField;
  }

  public static final class NoteBuilder {

    Long id;
    ZonedDateTime created_at;
    Long model_id;
    ZonedDateTime modified_at;
    String tags;
    String fields;
    String sortField;

    private NoteBuilder() {}

    public static NoteBuilder aNote() { return new NoteBuilder(); }

    public NoteBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public NoteBuilder withCreated_at(ZonedDateTime created_at) {
      this.created_at = created_at;
      return this;
    }

    public NoteBuilder withModel_id(Long model_id) {
      this.model_id = model_id;
      return this;
    }

    public NoteBuilder withModified_at(ZonedDateTime modified_at) {
      this.modified_at = modified_at;
      return this;
    }

    public NoteBuilder withTags(String tags) {
      this.tags = tags;
      return this;
    }

    public NoteBuilder withFields(String fields) {
      this.fields = fields;
      return this;
    }

    public NoteBuilder withSortField(String sortField) {
      this.sortField = sortField;
      return this;
    }

    public Note build() {
      Note note = new Note();
      note.id = this.id;
      note.model_id = this.model_id;
      note.created_at = this.created_at;
      note.modified_at = this.modified_at;
      note.fields = this.fields;
      note.tags = this.tags;
      note.sortField = this.sortField;
      return note;
    }
  }
}
