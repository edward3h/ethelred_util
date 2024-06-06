package org.ethelred.util.picocli.defaults;

import com.electronwill.nightconfig.core.file.FileConfig;
import org.ethelred.util.picocli.defaults.config.ConfigHelper;
import org.ethelred.util.picocli.defaults.config.SpecCommandView;
import picocli.CommandLine;

@CommandLine.Command(name = "config")
public class ConfigCommand implements Runnable {
    @CommandLine.Spec
    private CommandLine.Model.CommandSpec commandSpec;

    private final ConfigHelper configHelper = new ConfigHelper();

    @Override
    public void run() {
        list();
    }

    @CommandLine.Command
    public void list() {
        var root = commandSpec.root();
        var config = configHelper.getConfig(new SpecCommandView(root));
        if (config instanceof FileConfig) {
            var fileConfig = (FileConfig) config;
            var path = fileConfig.getNioPath();
            System.out.printf("Read config from '%s'%n", path);
        }
        if (config.isEmpty()) {
            System.out.println("No config entries.");
        }
        config.entrySet().forEach(entry -> System.out.printf("%s = %s%n", entry.getKey(), entry.getValue()));
    }
}
