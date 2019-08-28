package com.robertsanek.data.etl.local.habitica;

import com.robertsanek.data.etl.local.habitica.jsonentities.JsonTask;

class HabiticaUtils {

  static com.robertsanek.data.etl.local.habitica.Task
  convertJsonTaskToHibernateTask(JsonTask jsonTask) {
    return Task.TaskBuilder.aTask()
        .withId(jsonTask.getId())
        .withName(jsonTask.getText())
        .withTimeInMinutes(Double.parseDouble(jsonTask.getNotes()))
        .build();
  }

}
