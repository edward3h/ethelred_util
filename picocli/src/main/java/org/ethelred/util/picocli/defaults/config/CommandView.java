package org.ethelred.util.picocli.defaults.config;

public interface CommandView {
    String applicationName();

    String[] aliases();

    String qualifier();

    String organisation();
}
