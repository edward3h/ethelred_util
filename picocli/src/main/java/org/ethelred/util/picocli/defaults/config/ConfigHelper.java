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
import java.util.stream.Collectors;

public class ConfigHelper {
    private Config foundConfig;

    public Config getConfig(CommandView commandView) {
        if (foundConfig == null) {
            var names = combine(commandView.applicationName(), commandView.aliases());
            var configDir = getConfigDir(commandView);
            var filePaths = generatePaths(configDir, names, supportedExtensions());
            var foundFiles = filePaths.stream().filter(Files::exists).collect(Collectors.toList());
            if (foundFiles.size() > 1) {
                throw new IllegalStateException("Found multiple config files: " + foundFiles);
            } else if (foundFiles.size() == 1) {
                foundConfig = FileConfig.of(foundFiles.get(0));
                ((FileConfig) foundConfig).load();
            } else {
                foundConfig = Config.inMemory();
            }
        }
        return foundConfig;
    }

    private static Path getConfigDir(CommandView commandView) {
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
                var configDir = getConfigDir(commandView);
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

    private static final List<String> KNOWN_EXTENSIONS = List.of("toml", "yaml", "yml");

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

    private static List<Path> generatePaths(Path configDir, List<String> names, List<String> extensions) {
        var homeDir = Path.of(System.getProperty("user.home"));
        var combinations = new ArrayList<Path>(names.size() * extensions.size() * 2);

        for (var extension : extensions) {
            for (var name : names) {
                combinations.add(configDir.resolve(name + "." + extension));
            }
        }
        for (var extension : extensions) {
            for (var name : names) {
                combinations.add(homeDir.resolve("." + name + "." + extension));
            }
        }
        return combinations;
    }
}
