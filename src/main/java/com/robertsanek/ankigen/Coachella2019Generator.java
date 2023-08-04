package com.robertsanek.ankigen;

import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.robertsanek.data.etl.remote.lastfm.Artist;
import com.robertsanek.data.quality.anki.DataQualityBase;

public class Coachella2019Generator extends ArtistGenerator {

  @Override
  public List<Artist> getArtists() throws Exception {
    Set<String> existingPeopleInAnkiDb = DataQualityBase.getExistingPeopleInAnkiDbLowerCased();
    String artists = Resources.toString(Resources.getResource("com/robertsanek/ankigen/coachella.csv"), Charsets.UTF_8);
    CSVParser csvRecords = CSVParser.parse(artists, CSVFormat.DEFAULT);
    return csvRecords.getRecords().stream()
        .map(pair -> Artist.ArtistBuilder.anArtist()
            .withName(pair.get(0))
            .withImageUrl(pair.get(1))
            .withPlayCount(Long.MAX_VALUE)
            .withFoundInAnki(existingPeopleInAnkiDb.contains(pair.get(0).toLowerCase()))
            .build())
        .toList();
  }

  @Override
  public String getSource() {
    return super.getSource() +
        "<br>Scraped from Coachella 2019 lineup at https://www.coachella.com/lineup/#/artists/alphabetical";
  }
}
