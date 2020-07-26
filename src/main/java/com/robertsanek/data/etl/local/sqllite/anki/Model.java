package com.robertsanek.data.etl.local.sqllite.anki;

import static com.robertsanek.data.etl.local.sqllite.anki.AnkiEtl.FIELDS_LIMIT;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anki_models")
public class Model {

  @Id
  Long id;
  String name;
  ZonedDateTime created_at;
  ZonedDateTime modified_at;
  @Deprecated Long deck_id;
  @Column(length = FIELDS_LIMIT)
  @Deprecated String fields;
  @Column(length = FIELDS_LIMIT)
  @Deprecated String templates;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public ZonedDateTime getCreated_at() {
    return created_at;
  }

  public ZonedDateTime getModified_at() {
    return modified_at;
  }

  @Deprecated
  public Long getDeck_id() {
    return deck_id;
  }

  @Deprecated
  public String getFields() {
    return fields;
  }

  @Deprecated
  public String getTemplates() {
    return templates;
  }

  public static final class ModelBuilder {

    Long id;
    String name;
    ZonedDateTime created_at;
    ZonedDateTime modified_at;
    @Deprecated Long deck_id;
    @Deprecated String fields;
    @Deprecated String templates;

    private ModelBuilder() {}

    public static ModelBuilder aModel() { return new ModelBuilder(); }

    public ModelBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public ModelBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public ModelBuilder withCreated_at(ZonedDateTime created_at) {
      this.created_at = created_at;
      return this;
    }

    public ModelBuilder withModified_at(ZonedDateTime modified_at) {
      this.modified_at = modified_at;
      return this;
    }

    @Deprecated
    public ModelBuilder withDeck_id(Long deck_id) {
      this.deck_id = deck_id;
      return this;
    }

    @Deprecated
    public ModelBuilder withFields(String fields) {
      this.fields = fields;
      return this;
    }

    @Deprecated
    public ModelBuilder withTemplates(String templates) {
      this.templates = templates;
      return this;
    }

    public Model build() {
      Model model = new Model();
      model.name = this.name;
      model.modified_at = this.modified_at;
      model.deck_id = this.deck_id;
      model.fields = this.fields;
      model.id = this.id;
      model.created_at = this.created_at;
      model.templates = this.templates;
      return model;
    }
  }
}
