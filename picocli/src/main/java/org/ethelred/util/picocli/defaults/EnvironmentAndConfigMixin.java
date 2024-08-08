/* (C) 2024 */
package org.ethelred.util.picocli.defaults;

import java.nio.file.Path;
import org.jspecify.annotations.Nullable;
import picocli.CommandLine;

@CommandLine.Command(
        defaultValueProvider = EnvironmentAndConfigDefaultValueProvider.class,
        subcommands = {ConfigCommand.class})
public class EnvironmentAndConfigMixin {
    @CommandLine.Option(
            names = "--config-path",
            description =
                    "Specify a path for the config file. Default: path is discovered based on the command name and conventional configuration directories for the OS")
    public @Nullable Path configPath;
}
