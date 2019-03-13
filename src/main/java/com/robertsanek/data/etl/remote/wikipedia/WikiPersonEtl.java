package com.robertsanek.data.etl.remote.wikipedia;

import java.time.LocalDate;
import java.util.List;

import com.robertsanek.data.etl.Etl;
import com.robertsanek.util.Unchecked;
import com.robertsanek.wikipedia.WikipediaConnector;

public class WikiPersonEtl extends Etl<WikiPerson> {

  @Override
  public List<WikiPerson> getObjects() {
    return Unchecked.get(() -> WikipediaConnector.getMostViewedPeople(WikipediaConnector.Language.EN,
        LocalDate.now().minusMonths(12),
        WikipediaConnector.Granularity.MONTHLY,
        100,
        false));
  }
}
