package com.robertsanek.data.etl.remote.oauth.toodledo;

import java.util.List;
import java.util.stream.Collectors;

import com.robertsanek.data.etl.Etl;

public class TaskEtl extends Etl<ToodledoTask> {

  @Override
  public List<ToodledoTask> getObjects() {
    return new ToodledoConnector().getTasks().stream()
        .map(ToodledoUtils::convertJsonTaskToHibernateTask)
        .collect(Collectors.toList());
  }
}
