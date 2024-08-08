/* (C) 2024 */
package org.ethelred.util.picocli.defaults.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.FormatDetector;
import dev.dirs.ProjectDirectories;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;

public class ConfigHelper {
    private static final List<String> supportedExtensions = supportedExtensions();
    private Config foundConfig;
    private List<String> commandNames;
    private Consumer<String> messager = str -> {};

    public Config getConfig(CommandView commandView) {
        if (foundConfig == null) {
            messager = commandView.out();
            commandNames = combine(commandView.applicationName(), commandView.aliases());
            var explicitPath = commandView.configPath();
            if (explicitPath != null) {
                // config path was given as a command line option
                return findInPath(explicitPath, true, "--config-path option");
            }
            var fromSystemProperty = System.getProperty("config.path");
            if (fromSystemProperty != null) {
                return findInPath(Path.of(fromSystemProperty), true, "System property \"config.path\"");
            }
            var fromEnv = System.getenv("CONFIG_PATH");
            if (fromEnv != null) {
                return findInPath(Path.of(fromEnv), true, "Environment variable $CONFIG_PATH");
            }
            var configDir = getConventionConfigDir(commandView);
            var fromConventionDir = findInPath(configDir, false, "Convention config directory");
            if (fromConventionDir != null) {
                return fromConventionDir;
            }
            var homeDir = Path.of(System.getProperty("user.home"));
            var fromHomeDir = findInPath(homeDir, false, "Home directory");
            if (fromHomeDir != null) {
                return fromHomeDir;
            }
            foundConfig = Config.inMemory();
        }
        return foundConfig;
    }

    private @Nullable Config findInPath(Path p, boolean throwIfNotFound, String source) {
        if (Files.isRegularFile(p)) {
            return found(p, source);
        } else if (Files.isDirectory(p)) {
            return findInDirectory(p, throwIfNotFound, source);
        }
        if (throwIfNotFound) {
            throw new IllegalStateException(String.format("Config file from %s not found at path \"%s\"", source, p));
        }
        return null;
    }

    private Config findInDirectory(Path p, boolean throwIfNotFound, String source) {
        var foundFiles = generatePaths(p).stream().filter(Files::exists).collect(Collectors.toList());
        if (foundFiles.size() == 1) {
            return found(foundFiles.get(0), source);
        } else if (foundFiles.size() > 1) {
            throw new IllegalStateException("Multiple config files found: " + foundFiles);
        }
        if (throwIfNotFound) {
            throw new IllegalStateException(String.format("Config file not found at path \"%s\"", p));
        }
        return null;
    }

    private Config found(Path p, String source) {
        var config = FileConfig.of(p);
        config.load();
        foundConfig = config;
        messager.accept(String.format("Read config file @|bold,cyan \"%s\"|@, from %s", p, source));
        return foundConfig;
    }

    private static Path getConventionConfigDir(CommandView commandView) {
        return Path.of(ProjectDirectories.from(
                        commandView.qualifier(), commandView.organisation(), commandView.applicationName())
                .configDir);
    }

    public void saveConfig(CommandView commandView, Config config) {
        try {
            FileConfig fileConfig;
            if (config instanceof FileConfig) {
                fileConfig = (FileConfig) config;
            } else {
                List<String> supportedExtensions = supportedExtensions();
                if (supportedExtensions.isEmpty()) {
                    throw new IllegalStateException("No supported config file format on classpath");
                }
                var configDir = getConventionConfigDir(commandView);
                Files.createDirectories(configDir);
                fileConfig = FileConfig.of(
                        configDir.resolve(commandView.applicationName() + "." + supportedExtensions.get(0)));
                fileConfig.addAll(config);
            }
            fileConfig.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static final List<String> KNOWN_EXTENSIONS = List.of("toml", "yaml", "yml", "properties");

    private static List<String> supportedExtensions() {
        return KNOWN_EXTENSIONS.stream()
                .filter(x -> FormatDetector.detectByName("fake." + x) != null)
                .collect(Collectors.toList());
    }

    private static List<String> combine(String first, String... more) {
        var r = new ArrayList<String>(more.length + 1);
        r.add(first);
        r.addAll(List.of(more));
        return List.copyOf(r);
    }

    private List<Path> generatePaths(Path configDir) {
        var combinations = new ArrayList<Path>(commandNames.size() * supportedExtensions.size() * 2);

        for (var extension : supportedExtensions) {
            for (var name : commandNames) {
                combinations.add(configDir.resolve(name + "." + extension));
            }
        }
        return combinations;
    }
}
