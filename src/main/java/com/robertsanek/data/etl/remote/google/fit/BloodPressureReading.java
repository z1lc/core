package com.robertsanek.data.etl.remote.google.fit;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "blood_pressure_readings")
public class BloodPressureReading {

  @Id
  private Long id;
  private ZonedDateTime date;
  private Long systolic;
  private Long diastolic;

  public static final class BloodPressureReadingBuilder {

    private Long id;
    private ZonedDateTime date;
    private Long systolic;
    private Long diastolic;

    private BloodPressureReadingBuilder() {}

    public static BloodPressureReadingBuilder aBloodPressureReading() {
      return new BloodPressureReadingBuilder();
    }

    public BloodPressureReadingBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public BloodPressureReadingBuilder withDate(ZonedDateTime date) {
      this.date = date;
      return this;
    }

    public BloodPressureReadingBuilder withSystolic(Long systolic) {
      this.systolic = systolic;
      return this;
    }

    public BloodPressureReadingBuilder withDiastolic(Long diastolic) {
      this.diastolic = diastolic;
      return this;
    }

    public BloodPressureReading build() {
      BloodPressureReading bloodPressureReading = new BloodPressureReading();
      bloodPressureReading.diastolic = this.diastolic;
      bloodPressureReading.id = this.id;
      bloodPressureReading.date = this.date;
      bloodPressureReading.systolic = this.systolic;
      return bloodPressureReading;
    }
  }
}
