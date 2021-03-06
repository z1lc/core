package com.robertsanek.data.etl.local.habitica;

import java.util.List;
import java.util.stream.Collectors;

public class TaskEtl extends HabiticaEtl<Task> {

  @Override
  public List<Task> getObjects() {
    return getJsonObjects().stream()
        .map(HabiticaUtils::convertJsonTaskToHibernateTask)
        .collect(Collectors.toList());
  }
}
