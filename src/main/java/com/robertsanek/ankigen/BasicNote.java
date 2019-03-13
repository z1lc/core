package com.robertsanek.ankigen;

import java.net.URI;
import java.util.List;

public class BasicNote {

  private String front;
  private String back;
  private boolean addReverse = true;
  private String context;
  private String extra;
  private String abbrevShort = "dummy_abbrev";
  private List<URI> frontImage;
  private List<URI> backImage;
  private List<URI> extraImage;
  private String frontSound = "dummy_sound";
  private boolean addTypeFront = true;
  private String source;

  public static final class BasicNoteBuilder {

    String front;
    String back;
    boolean addReverse;
    String context;
    String extra;
    String abbrevShort = "dummy_abbrev";
    List<URI> frontImage;
    List<URI> backImage;
    List<URI> extraImage;
    String frontSound = "dummy_sound";
    boolean addTypeFront = true;
    String source;

    private BasicNoteBuilder() {}

    public static BasicNoteBuilder aBasicNote() {
      return new BasicNoteBuilder();
    }

    public BasicNoteBuilder withFront(String front) {
      this.front = front;
      return this;
    }

    public BasicNoteBuilder withBack(String back) {
      this.back = back;
      return this;
    }

    public BasicNoteBuilder withAddReverse(boolean addReverse) {
      this.addReverse = addReverse;
      return this;
    }

    public BasicNoteBuilder withContext(String context) {
      this.context = context;
      return this;
    }

    public BasicNoteBuilder withExtra(String extra) {
      this.extra = extra;
      return this;
    }

    public BasicNoteBuilder withAbbrevShort(String abbrevShort) {
      this.abbrevShort = abbrevShort;
      return this;
    }

    public BasicNoteBuilder withFrontImage(List<URI> frontImage) {
      this.frontImage = frontImage;
      return this;
    }

    public BasicNoteBuilder withBackImage(List<URI> backImage) {
      this.backImage = backImage;
      return this;
    }

    public BasicNoteBuilder withExtraImage(List<URI> extraImage) {
      this.extraImage = extraImage;
      return this;
    }

    public BasicNoteBuilder withFrontSound(String frontSound) {
      this.frontSound = frontSound;
      return this;
    }

    public BasicNoteBuilder withAddTypeFront(boolean addTypeFront) {
      this.addTypeFront = addTypeFront;
      return this;
    }

    public BasicNoteBuilder withSource(String source) {
      this.source = source;
      return this;
    }

    public BasicNote build() {
      BasicNote basicNote = new BasicNote();
      basicNote.abbrevShort = this.abbrevShort;
      basicNote.extraImage = this.extraImage;
      basicNote.source = this.source;
      basicNote.addReverse = this.addReverse;
      basicNote.back = this.back;
      basicNote.backImage = this.backImage;
      basicNote.context = this.context;
      basicNote.extra = this.extra;
      basicNote.frontImage = this.frontImage;
      basicNote.addTypeFront = this.addTypeFront;
      basicNote.front = this.front;
      basicNote.frontSound = this.frontSound;
      return basicNote;
    }
  }
}
