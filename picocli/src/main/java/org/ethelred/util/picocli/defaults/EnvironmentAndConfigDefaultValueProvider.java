package org.ethelred.util.picocli.defaults;

public class EnvironmentAndConfigDefaultValueProvider extends CascadingDefaultValueProvider {
    public EnvironmentAndConfigDefaultValueProvider() {
        super(new EnvironmentDefaultValueProvider(), new ConfigDefaultValueProvider());
    }
}
