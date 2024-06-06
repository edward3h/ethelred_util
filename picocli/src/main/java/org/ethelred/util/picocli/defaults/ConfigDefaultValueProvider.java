/* (C) 2024 */
package org.ethelred.util.picocli.defaults;

import com.electronwill.nightconfig.core.Config;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.ethelred.util.picocli.defaults.config.ConfigHelper;
import org.ethelred.util.picocli.defaults.config.SpecCommandView;
import picocli.CommandLine;

public class ConfigDefaultValueProvider implements CommandLine.IDefaultValueProvider {
    private final ConfigHelper configHelper = new ConfigHelper();

    @Override
    public String defaultValue(CommandLine.Model.ArgSpec argSpec) {
        var commandSpec = argSpec.command();
        var view = new SpecCommandView(commandSpec);
        var config = configHelper.getConfig(view);
        return findValue(config, commandSpec, argSpec);
    }

    private String findValue(Config config, CommandLine.Model.CommandSpec spec, CommandLine.Model.ArgSpec argSpec) {
        var argKey = argSpec.descriptionKey();
        if (argKey == null && argSpec instanceof CommandLine.Model.OptionSpec) {
            argKey = ((CommandLine.Model.OptionSpec) argSpec).longestName().replaceAll("^-+", "");
        }
        var commandKey = spec.qualifiedName(".");
        var value = config.get(commandKey + "." + argKey);
        if (value instanceof Collection) {
            value = handleCollection((Collection<?>) value, argSpec);
        }
        return value == null ? null : value.toString();
    }

    private Object handleCollection(Collection<?> collection, CommandLine.Model.ArgSpec argSpec) {
        if (collection.isEmpty()) {
            return null;
        }
        if (!argSpec.isMultiValue() || argSpec.splitRegex() == null) {
            return collection.iterator().next();
        }
        var joinChar = Objects.requireNonNullElse(argSpec.splitRegexSynopsisLabel(), argSpec.splitRegex());
        return collection.stream().map(String::valueOf).collect(Collectors.joining(joinChar));
    }
}
