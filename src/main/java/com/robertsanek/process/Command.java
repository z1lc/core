package com.robertsanek.process;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;

public enum Command {
  DAEMON(ImmutableList.of("daemon")),
  DQ(ImmutableList.of("dq")),
  ETL(ImmutableList.of("etl")),
  ETL_SETUP(ImmutableList.of("etl_setup")),
  HABITICA(ImmutableList.of("habitica")),
  KIVA(ImmutableList.of("kiva")),
  WIKI(ImmutableList.of("wiki"));

  private final ImmutableList<String> possibleArgs;

  Command(ImmutableList<String> possibleArgs) {
    this.possibleArgs = possibleArgs;
  }

  public static Optional<Command> matchToCommand(String arg) {
    final String lowerArg = arg.toLowerCase();
    List<Command> potentialCommand = Arrays.stream(Command.values())
        .filter(command -> command.getPossibleArgs().contains(lowerArg))
        .toList();
    if (potentialCommand.size() != 1) {
      return Optional.empty();
    } else {
      return Optional.of(potentialCommand.get(0));
    }
  }

  public List<String> getPossibleArgs() {
    return possibleArgs;
  }
}
