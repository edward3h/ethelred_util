/* (C) 2024 */
package org.ethelred.util.picocli.defaults;

import java.util.function.Function;
import picocli.CommandLine.IDefaultValueProvider;
import picocli.CommandLine.Model.ArgSpec;

public class EnvironmentDefaultValueProvider implements IDefaultValueProvider {

    protected static Function<String, String> envLoader = System::getenv;

    @Override
    public String defaultValue(ArgSpec argSpec) {
        var propertyKey = PropertyKey.from(argSpec);
        if (propertyKey != null) {
            return envLoader.apply(propertyKey.asEnvironmentVariable());
        }
        return null;
    }
}
