/* (C) 2024 */
package org.ethelred.util.picocli.examples;

import org.ethelred.util.picocli.defaults.EnvironmentAndConfigMixin;
import picocli.CommandLine;

@CommandLine.Command(name = "example-mixin", mixinStandardHelpOptions = true)
public class MixinExampleApp implements Runnable {
    public static void main(String[] args) {
        new CommandLine(new MixinExampleApp()).execute(args);
    }

    @CommandLine.Mixin
    EnvironmentAndConfigMixin mixin;

    public void run() {
        System.out.println("run");
    }
}
