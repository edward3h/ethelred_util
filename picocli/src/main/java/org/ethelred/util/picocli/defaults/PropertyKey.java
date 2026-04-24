/* (C) 2024 */
package org.ethelred.util.picocli.defaults;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;
import picocli.CommandLine;

public class PropertyKey {
    private final List<String> commands;
    private final List<String> propertySegments;

    private PropertyKey(List<String> commands, List<String> propertySegments) {
        this.commands = commands;
        this.propertySegments = propertySegments;
    }

    public String asEnvironmentVariable() {
        return Stream.of(commands, propertySegments)
                .flatMap(List::stream)
                .map(String::toUpperCase)
                .collect(Collectors.joining("_"));
    }

    public static @Nullable PropertyKey from(CommandLine.Model.ArgSpec argSpec) {
        var argKey = argSpec.descriptionKey();
        if ((argKey == null || argKey.isBlank()) && argSpec instanceof CommandLine.Model.OptionSpec) {
            argKey = ((CommandLine.Model.OptionSpec) argSpec).longestName();
        }
        if (argKey == null || argKey.isBlank()) {
            return null;
        }
        var propertySegments = splitNonWord(argKey);
        var commands = argSpec.command().qualifiedName().split("\\s+");
        return new PropertyKey(List.of(commands), propertySegments);
    }

    private static List<String> splitNonWord(String argKey) {
        return Stream.of(argKey.split("\\W+"))
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyKey that = (PropertyKey) o;
        return Objects.equals(commands, that.commands) && Objects.equals(propertySegments, that.propertySegments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commands, propertySegments);
    }

    @Override
    public String toString() {
        return "PropertyKey{" + "commands=" + commands + ", propertySegments=" + propertySegments + '}';
    }
}
