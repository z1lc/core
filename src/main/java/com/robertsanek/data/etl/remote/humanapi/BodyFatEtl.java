package com.robertsanek.data.etl.remote.humanapi;

import java.util.List;

import com.robertsanek.data.etl.remote.humanapi.entities.GenericReading;

public class BodyFatEtl extends HumanApiEtl<GenericReading> {

  @Override
  public List<GenericReading> getObjects() {
    return genericGet("/v1/human/body_fat/readings", GenericReading[].class);
  }

}
