/* (C) 2024 */
package org.ethelred.util.picocli.defaults;

import picocli.CommandLine;

public class EnvironmentAndConfigTransformer implements CommandLine.IModelTransformer {
    @Override
    public CommandLine.Model.CommandSpec transform(CommandLine.Model.CommandSpec commandSpec) {
        var mixin = CommandLine.Model.CommandSpec.forAnnotatedObject(new EnvironmentAndConfigMixin());
        commandSpec.addMixin("config", mixin);
        return commandSpec;
    }
}
