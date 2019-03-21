package com.robertsanek.data.etl.remote.scrape.toodledo;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;

abstract class ToodledoHabitsEtl<T> extends Etl<T> {

  private static final String USERNAME = CommonProvider.getEmailAddress();
  private static ObjectMapper mapper = CommonProvider.getObjectMapper();

  <O> List<O> genericGet(String path, Class<O[]> clazz) {
    try (WebClient webClient = CommonProvider.getHtmlUnitWebClient()) {
      //via https://stackoverflow.com/a/31496516
      HtmlPage page = webClient.getPage("https://www.toodledo.com/signin.php");
      HtmlForm form = Iterables.getOnlyElement(page.getForms());
      form.getInputByName("email").setValueAttribute(USERNAME);
      HtmlInput passWordInput = form.getInputByName("pass");
      passWordInput.setValueAttribute(CommonProvider.getSecret(SecretType.TOODLEDO_PASSWORD));
      form.getElementsByTagName("button").get(0).click();
      Page page1 = webClient.getPage(path);

      return Lists
          .newArrayList(Unchecked.get(() -> mapper.readValue(page1.getWebResponse().getContentAsString(), clazz)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
