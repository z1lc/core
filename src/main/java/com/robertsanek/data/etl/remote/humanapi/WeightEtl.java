package com.robertsanek.data.etl.remote.humanapi;

import java.util.List;

import com.robertsanek.data.etl.remote.humanapi.entities.GenericReading;

public class WeightEtl extends HumanApiEtl<GenericReading> {

  @Override
  public List<GenericReading> getObjects() {
    return genericGet("/v1/human/weight/readings", GenericReading[].class);
  }

}
