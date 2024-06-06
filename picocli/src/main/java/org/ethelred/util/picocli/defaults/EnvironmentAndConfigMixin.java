package org.ethelred.util.picocli.defaults;

import picocli.CommandLine;

@CommandLine.Command(
        defaultValueProvider = EnvironmentAndConfigDefaultValueProvider.class,
        subcommands = {ConfigCommand.class})
public class EnvironmentAndConfigMixin {
}
