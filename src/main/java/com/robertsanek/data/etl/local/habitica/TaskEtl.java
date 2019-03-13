package com.robertsanek.data.etl.local.habitica;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.robertsanek.data.etl.DoNotRun;
import com.robertsanek.data.etl.local.habitica.jsonentities.JsonTask;
import com.robertsanek.data.etl.local.habitica.jsonentities.Response;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Unchecked;

@DoNotRun(explanation = "don't care about habitica anymore")
public class TaskEtl extends HabiticaEtl<Task> {

  private static ObjectMapper mapper = CommonProvider.getObjectMapper();

  public List<JsonTask> getJsonObjects() {
    String responseJson = HabiticaUtils.genericGetJson("api/v3/tasks/user", Lists
        .newArrayList(new BasicNameValuePair("type", "dailys")));
    Response response = Unchecked.get(() -> mapper.readValue(responseJson, Response.class));
    return response.getData();
  }

  @Override
  public List<Task> getObjects() {
    return getJsonObjects().stream()
        .map(HabiticaUtils::convertJsonTaskToHibernateTask)
        .collect(Collectors.toList());
  }
}
