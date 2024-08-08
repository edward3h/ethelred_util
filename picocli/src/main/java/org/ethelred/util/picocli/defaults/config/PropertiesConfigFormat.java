/* (C) 2024 */
package org.ethelred.util.picocli.defaults.config;

import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.ConfigParser;
import com.electronwill.nightconfig.core.io.ConfigWriter;
import com.electronwill.nightconfig.core.io.WritingException;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

public class PropertiesConfigFormat implements ConfigFormat<FileConfig>, ConfigWriter {
    @Override
    public ConfigWriter createWriter() {
        return this;
    }

    @Override
    public ConfigParser<FileConfig> createParser() {
        return null;
    }

    @Override
    public FileConfig createConfig(Supplier<Map<String, Object>> mapCreator) {
        return null;
    }

    @Override
    public boolean supportsComments() {
        return false;
    }

    @Override
    public void write(UnmodifiableConfig config, Writer writer) {
        var p = new Properties();
        for (var entry : config.entrySet()) {
            p.setProperty(entry.getKey(), entry.getRawValue());
        }
        try {
            p.store(writer, "");
        } catch (IOException e) {
            throw new WritingException(e);
        }
    }
}
