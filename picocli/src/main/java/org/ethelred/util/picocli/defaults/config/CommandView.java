/* (C) 2024 */
package org.ethelred.util.picocli.defaults.config;

import java.nio.file.Path;
import java.util.function.Consumer;
import org.jspecify.annotations.Nullable;

public interface CommandView {
    String applicationName();

    String[] aliases();

    String qualifier();

    String organisation();

    @Nullable Path configPath();

    Consumer<String> out();
}
