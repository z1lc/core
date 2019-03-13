package com.robertsanek.data.etl.remote.rescuetime;

import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rescuetime_daily_efficiencies")
public class Efficiency {

  @Id
  public ZonedDateTime date;
  @Column(name = "time_spent_seconds")
  public Long timeSpentSeconds;
  public Double efficiency;
  @Column(name = "efficiency_percent")
  public Double efficiencyPercent;

  public Efficiency() { }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Efficiency that = (Efficiency) o;
    return Objects.equals(date, that.date) &&
        Objects.equals(timeSpentSeconds, that.timeSpentSeconds) &&
        Objects.equals(efficiency, that.efficiency) &&
        Objects.equals(efficiencyPercent, that.efficiencyPercent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, timeSpentSeconds, efficiency, efficiencyPercent);
  }

  public static final class EfficiencyBuilder {

    public ZonedDateTime date;
    public Long timeSpentSeconds;
    public Double efficiency;
    public Double efficiencyPercent;

    private EfficiencyBuilder() {}

    public static EfficiencyBuilder anEfficiency() { return new EfficiencyBuilder(); }

    public EfficiencyBuilder withDate(ZonedDateTime date) {
      this.date = date;
      return this;
    }

    public EfficiencyBuilder withTimeSpentSeconds(Long timeSpentSeconds) {
      this.timeSpentSeconds = timeSpentSeconds;
      return this;
    }

    public EfficiencyBuilder withEfficiency(Double efficiency) {
      this.efficiency = efficiency;
      return this;
    }

    public EfficiencyBuilder withEfficiencyPercent(Double efficiencyPercent) {
      this.efficiencyPercent = efficiencyPercent;
      return this;
    }

    public Efficiency build() {
      Efficiency efficiency = new Efficiency();
      efficiency.efficiency = this.efficiency;
      efficiency.efficiencyPercent = this.efficiencyPercent;
      efficiency.timeSpentSeconds = this.timeSpentSeconds;
      efficiency.date = this.date;
      return efficiency;
    }
  }
}
