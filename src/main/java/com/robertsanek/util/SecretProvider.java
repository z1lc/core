package com.robertsanek.util;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.robertsanek.util.platform.CrossPlatformUtils;

@Singleton
public class SecretProvider {

  private static final Log log = Logs.getLog(SecretProvider.class);

  @Inject ObjectMapper objectMapper;

  private Map<SecretType, Secret> secrets;

  public String getSecret(SecretType secretType) {
    ensureInitialized();
    Optional<Secret> maybeSecret = Optional.ofNullable(secrets.get(secretType));
    if (maybeSecret.isPresent()) {
      return maybeSecret.orElseThrow().getSecret();
    } else {
      throw new RuntimeException(String.format("secrets.json file has no mapping for SecretType '%s'.", secretType));
    }
  }

  private void ensureInitialized() {
    if (secrets == null) {
      String secretsLocation = Optional.ofNullable(System.getenv("Z1LC_CORE_SECRETS_FILE_LOCATION"))
          .orElse(CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "secrets.json");
      File secretsFile = new File(secretsLocation);
      if (secretsFile.exists()) {
        secrets = Arrays.stream(Unchecked.get(() -> objectMapper
            .readValue(secretsFile, Secret[].class)))
            .collect(Collectors.toMap(Secret::getType, Function.identity()));
      } else {
        secrets = Arrays.stream(SecretType.values())
            .map(secretType -> new Secret(secretType, "", new HashMap<>()))
            .collect(Collectors.toMap(Secret::getType, Function.identity()));
        log.error("No file was found at '%s'.", secretsFile.toString());
      }
    }
  }
}
