package com.robertsanek.ankigen;

import java.net.URI;
import java.time.Year;
import java.util.List;

public class PersonNote {

  private String name;
  private String namePronunciation = "dummy_pron";
  private String knownFor = "dummy_known_for";
  private Year born;
  private Year died;
  private String context;
  private List<URI> image;
  private String source;

  public String getName() {
    return name;
  }

  public String getNamePronunciation() {
    return namePronunciation;
  }

  public String getKnownFor() {
    return knownFor;
  }

  public Year getBorn() {
    return born;
  }

  public Year getDied() {
    return died;
  }

  public String getContext() {
    return context;
  }

  public List<URI> getImage() {
    return image;
  }

  public String getSource() {
    return source;
  }

  public static final class PersonNoteBuilder {

    String name;
    String namePronunciation = "dummy_pron";
    String knownFor = "dummy_known_for";
    Year born;
    Year died;
    String context;
    List<URI> image;
    String source;

    private PersonNoteBuilder() {}

    public static PersonNoteBuilder aPersonNote() {
      return new PersonNoteBuilder();
    }

    public PersonNoteBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public PersonNoteBuilder withNamePronunciation(String namePronunciation) {
      this.namePronunciation = namePronunciation;
      return this;
    }

    public PersonNoteBuilder withKnownFor(String knownFor) {
      this.knownFor = knownFor;
      return this;
    }

    public PersonNoteBuilder withBorn(Year born) {
      this.born = born;
      return this;
    }

    public PersonNoteBuilder withDied(Year died) {
      this.died = died;
      return this;
    }

    public PersonNoteBuilder withContext(String context) {
      this.context = context;
      return this;
    }

    public PersonNoteBuilder withImage(List<URI> image) {
      this.image = image;
      return this;
    }

    public PersonNoteBuilder withSource(String source) {
      this.source = source;
      return this;
    }

    public PersonNote build() {
      PersonNote personNote = new PersonNote();
      personNote.namePronunciation = this.namePronunciation;
      personNote.born = this.born;
      personNote.died = this.died;
      personNote.image = this.image;
      personNote.context = this.context;
      personNote.knownFor = this.knownFor;
      personNote.source = this.source;
      personNote.name = this.name;
      return personNote;
    }
  }
}
