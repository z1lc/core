package com.robertsanek.data.etl.remote.lastfm;

import static com.robertsanek.util.SecretType.LAST_FM_API_KEY;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.remote.lastfm.jsonentities.ArtistApiResponse;
import com.robertsanek.data.etl.remote.lastfm.jsonentities.Image;
import com.robertsanek.data.quality.anki.DataQualityBase;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Unchecked;

public class ArtistEtl extends Etl<Artist> {

  private static ObjectMapper mapper = CommonProvider.getObjectMapper();
  private static final String USERNAME = "rsanek";

  @Override
  public List<Artist> getObjects() {
    Set<String> existingPeopleInAnkiDb = DataQualityBase.getExistingPeopleInAnkiDbLowerCased();
    return IntStream.range(1, extractTotalPages(getApiResponse(1, 1)) + 1).parallel()
        .boxed()
        .flatMap(page -> getApiResponse(page, 1000).getTopartists().getArtist().stream())
        .map(artist -> {
          String name = DataQualityBase.cleanName(artist.getName());
          return Artist.ArtistBuilder.anArtist()
              .withRank(Long.valueOf(artist.getAttr().getRank()))
              .withName(name)
              .withPlayCount(Long.valueOf(artist.getPlaycount()))
              .withFoundInAnki(existingPeopleInAnkiDb.contains(name.toLowerCase()))
              .withImageUrl(Iterables.getOnlyElement(artist.getImage().stream()
                  .filter(image -> image.getSize().equals("mega"))
                  .map(Image::getText)
                  .collect(Collectors.toList())))
              .build();
        })
        .collect(Collectors.toList());
  }

  private ArtistApiResponse getApiResponse(long pageNumber, long limitPerPage) {
    URI uri = Unchecked.get(() -> new URIBuilder()
        .setScheme("https")
        .setHost("ws.audioscrobbler.com")
        .setPath("/2.0/")
        .setParameter("method", "user.gettopartists")
        .setParameter("user", USERNAME)
        .setParameter("format", "json")
        .setParameter("period", "overall")
        .setParameter("api_key", CommonProvider.getSecret(LAST_FM_API_KEY))
        .setParameter("page", String.valueOf(pageNumber))
        .setParameter("limit", String.valueOf(limitPerPage))
        .build());
    return Unchecked.get(() -> mapper.readValue(Request.Get(uri)
            .execute()
            .returnContent()
            .asString(Charsets.UTF_8),
        ArtistApiResponse.class));
  }

  private int extractTotalPages(ArtistApiResponse response) {
    return (int) Math.ceil(Double.valueOf(response.getTopartists().getAttr().getTotal()) / 1000);
  }

}
