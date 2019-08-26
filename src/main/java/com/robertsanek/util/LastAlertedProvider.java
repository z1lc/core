package com.robertsanek.util;

import java.time.ZonedDateTime;

import com.google.inject.ImplementedBy;
import com.robertsanek.process.Command;
import com.robertsanek.process.SuccessType;

@ImplementedBy(LastAlertedProviderImpl.class)
public interface LastAlertedProvider {

  ZonedDateTime getLast(Command command, SuccessType successType);

}
