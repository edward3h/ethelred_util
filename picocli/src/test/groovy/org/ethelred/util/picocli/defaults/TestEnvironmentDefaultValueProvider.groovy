package org.ethelred.util.picocli.defaults

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvEntry
import spock.lang.Specification
import picocli.CommandLine

/**
*/
class TestEnvironmentDefaultValueProvider extends Specification implements CommandLine.IFactory {
    Map<String, String> env = [:]

    def setup() {
    }

    def 'value from option'() {
        when:
        def app = new FakeApp()
        new CommandLine(app, this).execute("--my-option", "vfo")

        then:
        app.myOption == "vfo"
    }

    def 'value from env'() {
        when:
            env['F_MY_OPTION'] = 'vfe'
            def app = new FakeApp()
            new CommandLine(app, this).execute()

        then:
            app.myOption == 'vfe'
    }

    def 'no value'() {
        when:
            def app = new FakeApp()
            new CommandLine(app, this).execute()

        then:
            app.myOption == 'fake_default'

    }

    def 'annotated env var option'() {
        when:
            env['AN_OPTION'] = 'is set'
            def app = new FakeApp()
            new CommandLine(app, this).execute()

        then:
            app.annotatedOption == 'is set'
    }

    def 'annotated option must not match auto property key'() {
        when:
            env['F_ANNOTATED_OPTION'] = 'is set'
            def app = new FakeApp()
            new CommandLine(app, this).execute()

        then:
            app.annotatedOption != 'is set'

    }

    @Override
    <K> K create(Class<K> cls) throws Exception {
        if (cls.equals(EnvironmentDefaultValueProvider)) {
            return new EnvironmentDefaultValueProvider(new MapDotenv(env)) as K
        }
        return CommandLine.defaultFactory().create(cls)
    }

    static class MapDotenv implements Dotenv {
        final Map<String, String> mapEnv

        MapDotenv(Map<String, String> mapEnv) {
            this.mapEnv = mapEnv
        }

        @Override
        Set<DotenvEntry> entries() {
            return null
        }

        @Override
        Set<DotenvEntry> entries(Filter filter) {
            return null
        }

        @Override
        String get(String key) {
            return mapEnv.get(key)
        }

        @Override
        String get(String key, String defaultValue) {
            return mapEnv.getOrDefault(key, defaultValue)
        }
    }

    @CommandLine.Command(name = "f", defaultValueProvider = EnvironmentDefaultValueProvider)
    static class FakeApp implements Runnable{
        @CommandLine.Option(names = ["--my-option"], defaultValue = "fake_default")
        String myOption

        @EnvironmentDefaultValueProvider.Env(["AN_OPTION"])
        @CommandLine.Option(names = ["--annotated-option"])
        String annotatedOption

        void run() {
            // do nothing
        }
    }
}
