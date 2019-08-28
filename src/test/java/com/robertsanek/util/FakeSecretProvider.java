package com.robertsanek.util;

public class FakeSecretProvider extends SecretProvider {

  @Override
  public String getSecret(SecretType secretType) {
    return "";
  }
}
