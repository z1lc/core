package com.robertsanek.data.etl.remote.oauth.toodledo;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.common.collect.Lists;
import com.robertsanek.data.etl.UsesLocalFiles;
import com.robertsanek.data.etl.remote.oauth.OAuth20Utils;
import com.robertsanek.data.etl.remote.oauth.toodledo.jsonentities.JsonTask;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

@UsesLocalFiles
public class ToodledoConnector {

  private static final String TOODLEDO_CLIENT_ID = CommonProvider.getSecret(SecretType.TOODLEDO_CLIENT_ID);
  private static final String TOODLEDO_CLIENT_SECRET = CommonProvider.getSecret(SecretType.TOODLEDO_CLIENT_SECRET);
  private static final String TOODLEDO_ROOT = CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/toodledo/";
  private static ObjectMapper mapper = CommonProvider.getObjectMapper();
  private final String secretState = RandomStringUtils.randomAlphanumeric(13).toLowerCase();
  private final OAuth20Service service = new ServiceBuilder(TOODLEDO_CLIENT_ID)
      .apiSecret(TOODLEDO_CLIENT_SECRET)
      .scope("basic tasks notes")
      .state(secretState)
      .build(new ToodledoApi20(TOODLEDO_CLIENT_ID, TOODLEDO_CLIENT_SECRET));

  public List<JsonTask> getTasks() {
    OAuth20Utils
        oAuth20Utils = new OAuth20Utils(service, TOODLEDO_ROOT, "https://api.toodledo.com/3/account/get.php?f=json");
    String requestUrl = "http://api.toodledo.com/3/tasks/get.php?after=1234567890&fields=folder,context,goal," +
        "location,tag,startdate,duedate,duedatemod,starttime,duetime,remind,repeat,status,star,priority,length,timer," +
        "added,note,parent,children,order,meta,previous,attachment,shared,addedby,via,attachments&comp=-1&num=1000";
    final Response response =
        oAuth20Utils.getSignedResponse(requestUrl, oAuth20Utils.maybeGetAccessToken().orElseThrow());
    JsonTask[] jsonTasks = Unchecked.get(() -> mapper.readValue(response.getBody(), JsonTask[].class));
    return Lists.newArrayList(jsonTasks).subList(1, jsonTasks.length);
  }

}
