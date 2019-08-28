package com.robertsanek.data.etl.remote.oauth.toodledo;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
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
  private static final String TOODLEDO_ROOT =
      CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/toodledo/";
  @Inject ObjectMapper mapper;
  private final OAuth20Service service = new ServiceBuilder(TOODLEDO_CLIENT_ID)
      .apiSecret(TOODLEDO_CLIENT_SECRET)
      .defaultScope("basic tasks notes")
      .build(new ToodledoApi20(TOODLEDO_CLIENT_ID, TOODLEDO_CLIENT_SECRET));

  public List<JsonTask> getTasks() {
    OAuth20Utils
        oAuth20Utils = new OAuth20Utils(service, TOODLEDO_ROOT, "https://api.toodledo.com/3/account/get.php?f=json");
    int startOffset = 0;
    List<JsonTask> toReturn = Lists.newArrayList();
    while (true) {
      String requestUrl = getRequestUrl(startOffset);
      final Response response =
          oAuth20Utils.getSignedResponse(requestUrl, oAuth20Utils.maybeGetAccessToken().orElseThrow());
      JsonTask[] jsonTasks = Unchecked.get(() -> mapper.readValue(response.getBody(), JsonTask[].class));
      toReturn.addAll(Lists.newArrayList(jsonTasks).subList(1, jsonTasks.length));
      if (jsonTasks.length < 1001) {
        break;
      }
      startOffset += jsonTasks.length - 1;
    }
    return toReturn;
  }

  private String getRequestUrl(int page) {
    return String.format("http://api.toodledo.com/3/tasks/get.php?after=1234567890&fields=folder,context,goal," +
        "location,tag,startdate,duedate,duedatemod,starttime,duetime,remind,repeat,status,star,priority,length,timer," +
        "added,note,parent,children,order,meta,previous,attachment,shared,addedby,via,attachments&comp=-1" +
        "&start=%s&num=1000", page);
  }

}
