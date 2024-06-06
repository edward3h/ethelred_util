package org.ethelred.util.picocli.defaults;

import java.util.List;
import picocli.CommandLine;

public class CascadingDefaultValueProvider implements CommandLine.IDefaultValueProvider {
    private final List<CommandLine.IDefaultValueProvider> providers;

    public CascadingDefaultValueProvider(List<CommandLine.IDefaultValueProvider> providers) {
        this.providers = providers;
    }

    public CascadingDefaultValueProvider(CommandLine.IDefaultValueProvider... providers) {
        this(List.of(providers));
    }

    @Override
    public String defaultValue(CommandLine.Model.ArgSpec argSpec) throws Exception {
        for (var provider : providers) {
            var value = provider.defaultValue(argSpec);
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}
