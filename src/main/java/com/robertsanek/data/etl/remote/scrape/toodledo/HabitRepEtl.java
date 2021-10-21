package com.robertsanek.data.etl.remote.scrape.toodledo;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.robertsanek.data.etl.DoNotRun;

@DoNotRun(explanation = "no longer use Toodledo")
public class HabitRepEtl extends ToodledoHabitsEtl<HabitRep> {

  AtomicLong counter = new AtomicLong(1);

  @Override
  public List<HabitRep> getObjects() {
    List<HabitRep> habitReps = genericGet("https://habits.toodledo.com/api/habits/daily_get.php", HabitRep[].class);
    habitReps.forEach(hr -> hr.setId(counter.getAndIncrement()));
    return habitReps;
  }
}
