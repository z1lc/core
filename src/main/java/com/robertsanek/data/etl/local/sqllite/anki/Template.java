package com.robertsanek.data.etl.local.sqllite.anki;

import static com.robertsanek.data.etl.local.sqllite.anki.AnkiEtl.FIELDS_LIMIT;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anki_templates")
public class Template {

  @Id
  Long id; //should be model id && template ordinal (concatenated, not added)
  Long model_id;
  Long deck_id;
  String name;
  @Column(length = FIELDS_LIMIT)
  String front_html;
  @Column(length = FIELDS_LIMIT)
  String back_html;
  Long ordinal;

  public Long getId() {
    return id;
  }

  public Long getModel_id() {
    return model_id;
  }

  public Long getDeck_id() {
    return deck_id;
  }

  public String getName() {
    return name;
  }

  public String getFront_html() {
    return front_html;
  }

  public String getBack_html() {
    return back_html;
  }

  public Long getOrdinal() {
    return ordinal;
  }

  public static final class TemplateBuilder {

    Long id; //should be model id && template ordinal (concatenated, not added)
    Long model_id;
    Long deck_id;
    String name;
    String front_html;
    String back_html;
    Long ordinal;

    private TemplateBuilder() {}

    public static TemplateBuilder aTemplate() { return new TemplateBuilder(); }

    public TemplateBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public TemplateBuilder withModel_id(Long model_id) {
      this.model_id = model_id;
      return this;
    }

    public TemplateBuilder withDeck_id(Long deck_id) {
      this.deck_id = deck_id;
      return this;
    }

    public TemplateBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public TemplateBuilder withFront_html(String front_html) {
      this.front_html = front_html;
      return this;
    }

    public TemplateBuilder withBack_html(String back_html) {
      this.back_html = back_html;
      return this;
    }

    public TemplateBuilder withOrdinal(Long ordinal) {
      this.ordinal = ordinal;
      return this;
    }

    public Template build() {
      Template template = new Template();
      template.id = this.id;
      template.back_html = this.back_html;
      template.name = this.name;
      template.model_id = this.model_id;
      template.deck_id = this.deck_id;
      template.front_html = this.front_html;
      template.ordinal = this.ordinal;
      return template;
    }
  }
}