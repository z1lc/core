package com.robertsanek.data.etl.remote.wikipedia;

import java.time.LocalDate;
import java.util.List;

import com.google.inject.Inject;
import com.robertsanek.data.etl.DoNotRun;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.SlowEtl;
import com.robertsanek.util.Unchecked;
import com.robertsanek.wikipedia.WikipediaConnector;

@SlowEtl
@DoNotRun(explanation = "still works, but questionable utility (should likely be in zdone now)")
public class WikiPersonEtl extends Etl<WikiPerson> {

  @Inject WikipediaConnector wikipediaConnector;

  @Override
  public List<WikiPerson> getObjects() {
    return Unchecked.get(() -> wikipediaConnector.getMostViewedPeople(WikipediaConnector.Language.EN,
        LocalDate.now().minusMonths(12),
        WikipediaConnector.Granularity.MONTHLY,
        100,
        false));
  }
}
