package com.robertsanek.ankigen;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.robertsanek.data.etl.remote.oauth.goodreads.GoodreadsBookEtl;

public class AuthorGenerator extends BaseGenerator {

  @Override
  public List<PersonNote> getPersons() {
    return new GoodreadsBookEtl().getObjects().stream()
        .filter(book -> !book.isFoundInAnki())
        .map(book -> Pair.of(book.getAuthorName(), book.getAuthorImageUrl()))
        .distinct()
        .map(pair -> PersonNote.PersonNoteBuilder.aPersonNote()
            .withName(pair.getLeft())
            .withImage(Lists.newArrayList(URI.create(pair.getRight())))
            .build())
        .collect(Collectors.toList());
  }
}
