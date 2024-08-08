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
        var propertyKey = PropertyKey.from(argSpec);
        return propertyKey == null ? null : findValue(config, propertyKey, argSpec);
    }

    private String findValue(Config config, PropertyKey key, CommandLine.Model.ArgSpec argSpec) {
        var value = key.configKeys().stream()
                .map(config::get)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
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
