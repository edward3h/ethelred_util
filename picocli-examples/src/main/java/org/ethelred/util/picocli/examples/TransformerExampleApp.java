package org.ethelred.util.picocli.examples;

import org.ethelred.util.picocli.defaults.EnvironmentAndConfigTransformer;
import picocli.CommandLine;

@CommandLine.Command(name = "example-transform", modelTransformer = EnvironmentAndConfigTransformer.class, mixinStandardHelpOptions = true)
public class TransformerExampleApp implements Runnable {
    public static void main(String[] args) {
        new CommandLine(new TransformerExampleApp()).execute(args);
    }

    @Override
    public void run() {}
}
