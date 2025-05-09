package com.robertsanek.data.etl.remote.scrape.toodledo;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.htmlunit.Page;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlPage;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;

abstract class ToodledoHabitsEtl<T> extends Etl<T> {

  @Inject ObjectMapper mapper;
  @Inject SecretProvider secretProvider;

  <O> List<O> genericGet(String path, Class<O[]> clazz) {
    try (WebClient webClient = CommonProvider.getHtmlUnitWebClient()) {
      //via https://stackoverflow.com/a/31496516
      HtmlPage page = webClient.getPage("https://www.toodledo.com/signin.php");
      HtmlForm form = page.getForms().stream().filter(f -> f.getId().equals("ToodledoLogin")).findFirst().get();
      form.getInputByName("email").setValueAttribute(getUsername());
      HtmlInput passWordInput = form.getInputByName("pass");
      passWordInput.setValueAttribute(getPassword());
      form.getElementsByTagName("button").get(0).click();
      Page page1 = webClient.getPage(path);

      return Lists
          .newArrayList(Unchecked.get(() -> mapper.readValue(page1.getWebResponse().getContentAsString(), clazz)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String getUsername() {
    return CommonProvider.getEmailAddress();
  }

  public String getPassword() {
    return secretProvider.getSecret(SecretType.TOODLEDO_PASSWORD);
  }

}
