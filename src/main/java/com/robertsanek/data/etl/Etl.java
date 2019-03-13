package com.robertsanek.data.etl;

import java.util.List;

public abstract class Etl<T> {

  public abstract List<T> getObjects() throws Exception;

}
