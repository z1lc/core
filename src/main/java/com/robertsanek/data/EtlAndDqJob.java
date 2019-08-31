package com.robertsanek.data;

import java.net.URI;
import java.util.Arrays;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.barnacle.AnkiEtl;
import com.barnacle.LeetCodeToodledoTaskEtl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.robertsanek.data.quality.anki.DataQualityRunner;
import com.robertsanek.process.MasterEtl;
import com.robertsanek.process.QuartzJob;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;

public class EtlAndDqJob implements QuartzJob {

  private static Log log = Logs.getLog(EtlAndDqJob.class);

  @Inject ObjectMapper mapper;
  @Inject ReCreateViews reCreateViews;
  @Inject SecretProvider secretProvider;
  @Inject AnkiEtl ankiEtl;
  @Inject LeetCodeToodledoTaskEtl leetCodeToodledoTaskEtl;
  @Inject MasterEtl masterEtl;
  @Inject DataQualityRunner dqRunner;

  public void exec(JobDataMap jobDataMap) {
    boolean fastRun = false;
    boolean parallel = true;
    if (jobDataMap != null) {
      fastRun = jobDataMap.getBoolean("fastrun");
      parallel = jobDataMap.getBoolean("parallel");
    }
    ankiEtl.call();
    leetCodeToodledoTaskEtl.run();
    boolean etlsSuccessful = masterEtl.runEtls(fastRun, parallel);

    log.info("Will execute all queries in CommonQueries.sql file to re-create views.");
    reCreateViews.executeQueries();

    if (etlsSuccessful) {
      triggerKlipfolioRefresh();
      dqRunner.exec(jobDataMap);
    } else {
      log.info("Not all ETLs were successful, so will not trigger Klipfolio refresh or run Data Quality checks.");
    }
  }

  @Override
  public void exec(JobExecutionContext context) {
    exec(context.getMergedJobDataMap());
  }

  @VisibleForTesting
  void triggerKlipfolioRefresh() {
    log.info("Triggering Klipfolio refresh for all data sources...");
    final URI uri = Unchecked.get(() -> new URIBuilder()
        .setScheme("https")
        .setHost("app.klipfolio.com")
        .setPath("api/1.0/datasources")
        .build());
    JsonObject obj =
        new JsonParser()
            .parse(Unchecked.get(() -> Request.Get(uri)
                .addHeader("kf-api-key", secretProvider.getSecret(SecretType.KLIPFOLIO_API_KEY))
                .execute()
                .returnContent()
                .asString()))
            .getAsJsonObject();
    String dataSources = obj.getAsJsonObject("data").getAsJsonArray("datasources").toString();
    DataSource[] dataSourcesArr = Unchecked.get(() -> mapper.readValue(dataSources, DataSource[].class));
    log.info("Will refresh %s data sources.", dataSourcesArr.length);

    URIBuilder refreshUriBuilder = new URIBuilder()
        .setScheme("https")
        .setHost("app.klipfolio.com");
    Arrays.stream(dataSourcesArr).parallel()
        .map(DataSource::getId)
        .forEach(id -> {
          URI refreshUri = Unchecked.get(() -> refreshUriBuilder
              .setPath(String.format("api/1.0/datasource-instances/%s/@/refresh", id))
              .build());
          try {
            HttpPost post = new HttpPost(refreshUri);
            post.addHeader("kf-api-key", secretProvider.getSecret(SecretType.KLIPFOLIO_API_KEY));
            CommonProvider.getHttpClient().execute(post);
          } catch (Exception e) {
            log.error(e);
          }
        });
    log.info("Successfully refreshed %s data sources.", dataSourcesArr.length);
  }

}
