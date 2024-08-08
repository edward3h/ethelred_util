/* (C) 2024 */
package org.ethelred.util.picocli.defaults.config;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.function.Consumer;
import org.ethelred.util.picocli.defaults.EnvironmentAndConfigMixin;
import org.jspecify.annotations.Nullable;
import picocli.CommandLine;

public class SpecCommandView implements CommandView {
    private final CommandLine.Model.CommandSpec commandSpec;
    private final String qualifier;
    private final String organisation;

    private final @Nullable Path configPath;
    private final PrintWriter out;

    public SpecCommandView(CommandLine.Model.CommandSpec commandSpec) {
        this.commandSpec = commandSpec.root();
        var obj = commandSpec.userObject();
        var packageName = obj.getClass().getPackageName();
        var split = packageName.split("\\.");
        // TODO unhealthy assumption about package names
        qualifier = split.length > 0 ? split[0] : "unknown";
        organisation = split.length > 1 ? split[1] : "unknown";
        configPath = getConfigPathFromMixin(commandSpec);
        out = commandSpec.commandLine().getOut();
    }

    private @Nullable Path getConfigPathFromMixin(CommandLine.Model.CommandSpec commandSpec) {
        var mixinSpec = commandSpec.mixins().get("config");
        if (mixinSpec != null) {
            var mixinObject = mixinSpec.userObject();
            if (mixinObject instanceof EnvironmentAndConfigMixin) {
                return ((EnvironmentAndConfigMixin) mixinObject).configPath;
            }
        }
        return null;
    }

    @Override
    public String applicationName() {
        return commandSpec.name();
    }

    @Override
    public String[] aliases() {
        return commandSpec.aliases();
    }

    @Override
    public String qualifier() {
        return qualifier;
    }

    @Override
    public String organisation() {
        return organisation;
    }

    @Override
    public Path configPath() {
        return configPath;
    }

    @Override
    public Consumer<String> out() {
        return str -> out.println(CommandLine.Help.Ansi.AUTO.string(str));
    }
}
