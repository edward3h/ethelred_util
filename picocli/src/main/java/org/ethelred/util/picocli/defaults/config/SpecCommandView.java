package org.ethelred.util.picocli.defaults.config;

import picocli.CommandLine;

public class SpecCommandView implements CommandView {
    private final CommandLine.Model.CommandSpec commandSpec;
    private final String qualifier;
    private final String organisation;

    public SpecCommandView(CommandLine.Model.CommandSpec commandSpec) {
        this.commandSpec = commandSpec.root();
        var obj = commandSpec.userObject();
        var packageName = obj.getClass().getPackageName();
        var split = packageName.split("\\.");
        // TODO unhealthy assumption about package names
        qualifier = split.length > 0 ? split[0] : "unknown";
        organisation = split.length > 1 ? split[1] : "unknown";
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
}
