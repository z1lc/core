package com.robertsanek.data.etl.remote.lastfm;

import static com.robertsanek.util.SecretType.LAST_FM_API_KEY;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.remote.lastfm.jsonentities.ArtistApiResponse;
import com.robertsanek.data.etl.remote.lastfm.jsonentities.Image;
import com.robertsanek.data.quality.anki.DataQualityBase;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Unchecked;

public class ArtistEtl extends Etl<Artist> {

  @Inject ObjectMapper mapper;
  private static final String USERNAME = "rsanek";
  private static final long ARTISTS_PER_PAGE = 1000;

  @Override
  public List<Artist> getObjects() {
    Set<String> existingPeopleInAnkiDb = DataQualityBase.getExistingPeopleInAnkiDbLowerCased();
    return IntStream.range(1, extractTotalPages(getApiResponse(1, 1)).orElse(0) + 1).parallel()
        .boxed()
        .flatMap(page -> getApiResponse(page, ARTISTS_PER_PAGE).getTopartists().getArtist().stream())
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

  @VisibleForTesting
  ArtistApiResponse getApiResponse(long pageNumber, long limitPerPage) {
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
    try {
      return mapper.readValue(get(uri), ArtistApiResponse.class);
    } catch (Exception e) {
      if (Pattern.compile("status code: 5\\d\\d").matcher(e.getMessage()).find()) {
        return new ArtistApiResponse();
      }
      throw new RuntimeException(e);
    }
  }

  @VisibleForTesting
  String get(URI uri) throws IOException {
    return Request.Get(uri)
        .execute()
        .returnContent()
        .asString(Charsets.UTF_8);
  }

  private Optional<Integer> extractTotalPages(ArtistApiResponse response) {
    return Optional.ofNullable(response.getTopartists())
        .map(topArtists -> (int) Math.ceil(Double.valueOf(topArtists.getAttr().getTotal()) / ARTISTS_PER_PAGE));
  }

}
