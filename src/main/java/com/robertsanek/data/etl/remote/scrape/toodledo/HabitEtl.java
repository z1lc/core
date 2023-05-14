package com.robertsanek.data.etl.remote.scrape.toodledo;

import java.util.List;

import com.robertsanek.data.etl.DoNotRun;

@DoNotRun(explanation = "Haven't used Toodledo in a while")
public class HabitEtl extends ToodledoHabitsEtl<Habit> {

  @Override
  public List<Habit> getObjects() {
    return genericGet("https://habits.toodledo.com/api/habits/habits_get.php", Habit[].class);
  }
}
