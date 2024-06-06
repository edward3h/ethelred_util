/* (C) 2024 */
package org.ethelred.util.picocli.defaults;

import java.util.function.Function;
import picocli.CommandLine.IDefaultValueProvider;
import picocli.CommandLine.Model.ArgSpec;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.OptionSpec;

public class EnvironmentDefaultValueProvider implements IDefaultValueProvider {

    protected static Function<String, String> envLoader = System::getenv;

    @Override
    public String defaultValue(ArgSpec argSpec) {
        if (argSpec.isOption()) {
            OptionSpec option = (OptionSpec) argSpec;
            CommandSpec command = option.command();
            String name = _normalize(command.qualifiedName("-") + "-" + option.longestName());
            return envLoader.apply(name);
        }
        return null;
    }

    private String _normalize(String input) {
        return input.toUpperCase().replaceAll("^\\W*", "").replaceAll("\\W+", "_");
    }
}
