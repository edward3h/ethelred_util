/* (C) 2024 */
package org.ethelred.util.picocli.defaults;

import static org.ethelred.util.collect.Sequences.concat;
import static org.ethelred.util.collect.Sequences.prefixes;
import static org.ethelred.util.collect.Sequences.tail;

import java.util.ArrayList;
import java.util.Collections;
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
    private final List<String> originalOption;

    private PropertyKey(List<String> commands, List<String> propertySegments, List<String> originalOption) {
        this.commands = commands;
        this.propertySegments = propertySegments;
        this.originalOption = originalOption;
    }

    public String asEnvironmentVariable() {
        return Stream.of(commands, propertySegments)
                .flatMap(List::stream)
                .map(String::toUpperCase)
                .collect(Collectors.joining("_"));
    }

    public List<List<String>> configKeys() {
        List<List<String>> result = new ArrayList<>(commands.size() + 2);
        if (commands.size() > 1) {
            var prefixes = new ArrayList<>(prefixes(tail(commands, -1)));
            Collections.reverse(prefixes);
            for (var prefix : prefixes) {
                result.add(concat(prefix, propertySegments));
            }
        }
        result.add(propertySegments);
        result.add(originalOption);
        return result;
    }

    public static @Nullable PropertyKey from(CommandLine.Model.ArgSpec argSpec) {
        var argKey = argSpec.descriptionKey();
        var original = List.<String>of();
        if ((argKey == null || argKey.isBlank()) && argSpec instanceof CommandLine.Model.OptionSpec) {
            argKey = ((CommandLine.Model.OptionSpec) argSpec).longestName();
            original = List.of(argKey.replaceFirst("^\\W+", ""));
        }
        if (argKey == null || argKey.isBlank()) {
            return null;
        }
        var propertySegments = splitNonWord(argKey);
        var commands = argSpec.command().qualifiedName().split("\\s+");
        return new PropertyKey(List.of(commands), propertySegments, original);
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

    List<String> propertySegments() {
        return propertySegments;
    }
}
