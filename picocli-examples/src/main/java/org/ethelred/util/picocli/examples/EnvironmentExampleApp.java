/* (C) 2024 */
package org.ethelred.util.picocli.examples;

import org.ethelred.util.picocli.defaults.EnvironmentDefaultValueProvider;
import picocli.CommandLine;

@CommandLine.Command(name = "foo", defaultValueProvider = EnvironmentDefaultValueProvider.class)
public class EnvironmentExampleApp implements Runnable {

    @CommandLine.Option(
            names = {"--my-option", "-m"},
            defaultValue = "nothing")
    private String myOption;

    public static void main(String[] args) {
        new CommandLine(new EnvironmentExampleApp()).execute(args);
    }

    @Override
    public void run() {
        System.out.println("My option was " + myOption);
    }
}
