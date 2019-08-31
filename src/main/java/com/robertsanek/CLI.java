package com.robertsanek;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.common.collect.Lists;
import com.robertsanek.process.Command;

public class CLI {

  class CliArgs {

    private Optional<Command> command;
    private boolean force;
    private boolean fastRun;
    private boolean parallel;

    CliArgs(Optional<Command> command, boolean force, boolean fastRun, boolean parallel) {
      this.command = command;
      this.force = force;
      this.fastRun = fastRun;
      this.parallel = parallel;
    }

    public Optional<Command> getCommand() {
      return command;
    }

    public boolean isForce() {
      return force;
    }

    public boolean isFastRun() {
      return fastRun;
    }

    public boolean isParallel() {
      return parallel;
    }
  }

  public CliArgs getCliArgs(String[] args) {
    Options options = new Options();
    Option commandSelection = new Option("c", "command", true,
        String.format("command to run; one of [%s]", Lists.newArrayList(Command.values())
            .stream()
            .flatMap(co -> co.getPossibleArgs().stream())
            .collect(Collectors.joining(", "))));
    commandSelection.setRequired(true);
    options.addOption(commandSelection);

    Option machineSelection = new Option("t", "type", true,
        String.format("run type; one of [%s]",
            String.join(", ", Lists.newArrayList("etl_machine", "manual"))));
    machineSelection.setRequired(true);
    options.addOption(machineSelection);

    Option parallelismSelection =
        new Option("p", "parallelism", true, "enable/disable parallelism; one of [true, false]. Default is true.");
    parallelismSelection.setRequired(false);
    options.addOption(parallelismSelection);

    Option forceDaemonSelection =
        new Option("f", "force", false, "when running daemon command, forces ETLs to be triggered immediately.");
    forceDaemonSelection.setRequired(false);
    options.addOption(forceDaemonSelection);

    Option fastRunSelection =
        new Option("fr", "fastrun", false,
            "limit to only run fast ETLs (such as when bandwidth/speed are concerns).");
    fastRunSelection.setRequired(false);
    options.addOption(fastRunSelection);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd;

    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      formatter.printHelp("core", options);
      return new CliArgs(null, false, false, false);
    }
    String parallelismPassed = cmd.getOptionValue("parallelism");
    boolean parallelism = parallelismPassed == null || Boolean.parseBoolean(parallelismPassed);
    return new CliArgs(
        Command.matchToCommand(cmd.getOptionValue("command")),
        Arrays.stream(cmd.getOptions()).anyMatch(opt -> opt.getLongOpt().equals("force")),
        Arrays.stream(cmd.getOptions()).anyMatch(opt -> opt.getLongOpt().equals("fastrun")),
        parallelism);
  }

}
