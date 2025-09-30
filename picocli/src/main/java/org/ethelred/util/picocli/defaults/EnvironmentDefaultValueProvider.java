/* (C) 2024 */
package org.ethelred.util.picocli.defaults;

import io.github.cdimascio.dotenv.Dotenv;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import picocli.CommandLine.IDefaultValueProvider;
import picocli.CommandLine.Model.ArgSpec;

public class EnvironmentDefaultValueProvider implements IDefaultValueProvider {

    private final Dotenv dotenv;

    public EnvironmentDefaultValueProvider(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    @SuppressWarnings("unused") // May be instantiated reflectively when used with Picocli
    public EnvironmentDefaultValueProvider() {
        this(Dotenv.configure().ignoreIfMissing().load());
    }

    @Override
    public String defaultValue(ArgSpec argSpec) {
        var obj = argSpec.userObject();
        if (obj instanceof AnnotatedElement) {
            var env = ((AnnotatedElement) obj).getAnnotation(Env.class);
            if (env != null) {
                for (var key : env.value()) {
                    var value = dotenv.get(key);
                    if (value != null && !value.isBlank()) {
                        return value;
                    }
                }
                return null; // if @Env was present, do not use PropertyKey
            }
        }
        var propertyKey = PropertyKey.from(argSpec);
        if (propertyKey != null) {
            return dotenv.get(propertyKey.asEnvironmentVariable());
        }
        return null;
    }

    /**
     * Use with {@link EnvironmentDefaultValueProvider} to specify environment variable names on Options.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.FIELD})
    public @interface Env {
        /**
         * Environment variable names that may be used to look up a value.
         */
        String[] value();
    }
}
