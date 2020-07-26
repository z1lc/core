package com.robertsanek.data.etl.local.sqllite.anki;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anki_fields")
public class Field {

  @Id
  Long id; //should be model id && field ordinal (concatenated, not added)
  Long model_id;
  String name;
  Long ordinal;
  @Deprecated String font_face;
  @Deprecated Long font_size;
  @Deprecated Boolean sticky;

  public Long getId() {
    return id;
  }

  public Long getModel_id() {
    return model_id;
  }

  public String getName() {
    return name;
  }

  public Long getOrdinal() {
    return ordinal;
  }

  @Deprecated
  public String getFont_face() {
    return font_face;
  }

  @Deprecated
  public Long getFont_size() {
    return font_size;
  }

  @Deprecated
  public Boolean getSticky() {
    return sticky;
  }

  @Override
  public String toString() {
    return "Field{" +
        "id=" + id +
        ", font_face='" + font_face + '\'' +
        ", font_size=" + font_size +
        ", model_id=" + model_id +
        ", name='" + name + '\'' +
        ", ordinal=" + ordinal +
        ", sticky=" + sticky +
        '}';
  }

  public static final class FieldBuilder {

    Long id; //should be model id && field ordinal (concatenated, not added)
    Long model_id;
    String name;
    Long ordinal;
    @Deprecated String font_face;
    @Deprecated Long font_size;
    @Deprecated Boolean sticky;

    private FieldBuilder() {}

    public static FieldBuilder aField() {
      return new FieldBuilder();
    }

    public FieldBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    @Deprecated
    public FieldBuilder withFont_face(String font_face) {
      this.font_face = font_face;
      return this;
    }

    @Deprecated
    public FieldBuilder withFont_size(Long font_size) {
      this.font_size = font_size;
      return this;
    }

    public FieldBuilder withModel_id(Long model_id) {
      this.model_id = model_id;
      return this;
    }

    public FieldBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public FieldBuilder withOrdinal(Long ordinal) {
      this.ordinal = ordinal;
      return this;
    }

    @Deprecated
    public FieldBuilder withSticky(Boolean sticky) {
      this.sticky = sticky;
      return this;
    }

    public Field build() {
      Field field = new Field();
      field.name = this.name;
      field.model_id = this.model_id;
      field.sticky = this.sticky;
      field.ordinal = this.ordinal;
      field.font_size = this.font_size;
      field.id = this.id;
      field.font_face = this.font_face;
      return field;
    }
  }
}
