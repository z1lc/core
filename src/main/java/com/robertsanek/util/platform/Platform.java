package com.robertsanek.util.platform;

import java.nio.file.Path;
import java.util.Optional;

public abstract class Platform {

  private final Path fileStorageDirectory;
  private final Path ankiBaseDirectory;
  private final Path ankiExecutable;
  private final Path desktop;

  public Platform(Path fileStorageDirectory, Path ankiBaseDirectory, Path ankiExecutable, Path desktop) {
    this.fileStorageDirectory = fileStorageDirectory;
    this.ankiBaseDirectory = ankiBaseDirectory;
    this.ankiExecutable = ankiExecutable;
    this.desktop = desktop;
  }

  public Optional<Path> getFileStorageDirectory() {
    return Optional.ofNullable(fileStorageDirectory);
  }

  public Optional<Path> getDesktop() {
    return Optional.ofNullable(desktop);
  }

  public Optional<Path> getAnkiBaseDirectory() {
    return Optional.ofNullable(ankiBaseDirectory);
  }

  public Optional<Path> getAnkiExecutable() {
    return Optional.ofNullable(ankiExecutable);
  }

  public abstract <T> T visit(Visitor<T> visitor);

  public interface Visitor<T> {

    T caseWindows10(Windows10 windows10);

    T caseRaspberryPi(RaspberryPi raspberryPi);

    T caseUbuntu(Ubuntu ubuntu);
  }

  public static class Windows10 extends Platform {

    public Windows10(Path fileStorageDirectory, Path ankiBaseDirectory, Path ankiExecutable, Path desktop) {
      super(fileStorageDirectory, ankiBaseDirectory, ankiExecutable, desktop);
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
      return visitor.caseWindows10(this);
    }
  }

  public static class RaspberryPi extends Platform {

    public RaspberryPi(Path fileStorageDirectory, Path ankiBaseDirectory, Path ankiExecutable, Path desktop) {
      super(fileStorageDirectory, ankiBaseDirectory, ankiExecutable, desktop);
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
      return visitor.caseRaspberryPi(this);
    }
  }

  public static class Ubuntu extends Platform {

    public Ubuntu(Path fileStorageDirectory, Path ankiBaseDirectory, Path ankiExecutable, Path desktop) {
      super(fileStorageDirectory, ankiBaseDirectory, ankiExecutable, desktop);
    }

    @Override
    public <T> T visit(Visitor<T> visitor) {
      return visitor.caseUbuntu(this);
    }
  }

}