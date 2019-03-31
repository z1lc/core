package com.robertsanek.data.etl;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "etl_runs")
public class EtlRun {

  @Id
  private Long id;
  private String class_name;
  private ZonedDateTime start_time;
  private ZonedDateTime end_time;
  private String thread_name;
  private Long rows_generated;
  private Boolean using_parallel;
  private Boolean was_successful;
  private Boolean is_slow;
  private Boolean uses_local_files;
  private Long seconds_in_extract_and_transform;
  private Long seconds_in_load;
  private Long try_number;

  public Long getId() {
    return id;
  }

  public String getClass_name() {
    return class_name;
  }

  public ZonedDateTime getStart_time() {
    return start_time;
  }

  public ZonedDateTime getEnd_time() {
    return end_time;
  }

  public String getThread_name() {
    return thread_name;
  }

  public Long getRows_generated() {
    return rows_generated;
  }

  public Boolean getUsing_parallel() {
    return using_parallel;
  }

  public Boolean getWas_successful() {
    return was_successful;
  }

  public Boolean getIs_slow() {
    return is_slow;
  }

  public Boolean getUses_local_files() {
    return uses_local_files;
  }

  public Long getSeconds_in_extract_and_transform() {
    return seconds_in_extract_and_transform;
  }

  public Long getSeconds_in_load() {
    return seconds_in_load;
  }

  public Long getTry_number() {
    return try_number;
  }

  public static final class EtlRunBuilder {

    private Long id;
    private String class_name;
    private ZonedDateTime start_time;
    private ZonedDateTime end_time;
    private String thread_name;
    private Long rows_generated;
    private Boolean using_parallel;
    private Boolean was_successful;
    private Boolean is_slow;
    private Boolean uses_local_files;
    private Long seconds_in_extract_and_transform;
    private Long seconds_in_load;
    private Long try_number;

    private EtlRunBuilder() {}

    public static EtlRunBuilder anEtlRun() {
      return new EtlRunBuilder();
    }

    public EtlRunBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public EtlRunBuilder withClass_name(String class_name) {
      this.class_name = class_name;
      return this;
    }

    public EtlRunBuilder withStart_time(ZonedDateTime start_time) {
      this.start_time = start_time;
      return this;
    }

    public EtlRunBuilder withEnd_time(ZonedDateTime end_time) {
      this.end_time = end_time;
      return this;
    }

    public EtlRunBuilder withThread_name(String thread_name) {
      this.thread_name = thread_name;
      return this;
    }

    public EtlRunBuilder withRows_generated(Long rows_generated) {
      this.rows_generated = rows_generated;
      return this;
    }

    public EtlRunBuilder withUsing_parallel(Boolean using_parallel) {
      this.using_parallel = using_parallel;
      return this;
    }

    public EtlRunBuilder withWas_successful(Boolean was_successful) {
      this.was_successful = was_successful;
      return this;
    }

    public EtlRunBuilder withIs_slow(Boolean is_slow) {
      this.is_slow = is_slow;
      return this;
    }

    public EtlRunBuilder withUses_local_files(Boolean uses_local_files) {
      this.uses_local_files = uses_local_files;
      return this;
    }

    public EtlRunBuilder withSeconds_in_extract_and_transform(Long seconds_in_extract_and_transform) {
      this.seconds_in_extract_and_transform = seconds_in_extract_and_transform;
      return this;
    }

    public EtlRunBuilder withSeconds_in_load(Long seconds_in_load) {
      this.seconds_in_load = seconds_in_load;
      return this;
    }

    public EtlRunBuilder withTry_number(Long try_number) {
      this.try_number = try_number;
      return this;
    }

    public EtlRun build() {
      EtlRun etlRun = new EtlRun();
      etlRun.end_time = this.end_time;
      etlRun.start_time = this.start_time;
      etlRun.class_name = this.class_name;
      etlRun.was_successful = this.was_successful;
      etlRun.id = this.id;
      etlRun.rows_generated = this.rows_generated;
      etlRun.using_parallel = this.using_parallel;
      etlRun.try_number = this.try_number;
      etlRun.is_slow = this.is_slow;
      etlRun.seconds_in_load = this.seconds_in_load;
      etlRun.thread_name = this.thread_name;
      etlRun.seconds_in_extract_and_transform = this.seconds_in_extract_and_transform;
      etlRun.uses_local_files = this.uses_local_files;
      return etlRun;
    }
  }
}
