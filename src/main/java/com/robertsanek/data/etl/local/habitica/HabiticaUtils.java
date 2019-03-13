package com.robertsanek.data.etl.local.habitica;

import java.net.URI;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import com.robertsanek.data.etl.local.habitica.jsonentities.JsonTask;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;

class HabiticaUtils {

  static com.robertsanek.data.etl.local.habitica.Task
  convertJsonTaskToHibernateTask(JsonTask jsonTask) {
    return Task.TaskBuilder.aTask()
        .withId(jsonTask.getId())
        .withName(jsonTask.getText())
        .withTime_in_minutes(jsonTask.getTimeBasedOnPriority())
        .build();
  }

  static String genericGetJson(String path, List<NameValuePair> parameter) {
    final URI uri = Unchecked.get(() -> new URIBuilder()
        .setScheme("https")
        .setHost("habitica.com")
        .setPath(path)
        .setParameters(parameter)
        .build());
    return Unchecked.get(() -> Request.Get(uri)
        .addHeader("x-api-key", CommonProvider.getSecret(SecretType.HABITICA_API_KEY))
        .addHeader("x-api-user", CommonProvider.getSecret(SecretType.HABITICA_USER))
        .execute()
        .returnContent()
        .asString());
  }

}
