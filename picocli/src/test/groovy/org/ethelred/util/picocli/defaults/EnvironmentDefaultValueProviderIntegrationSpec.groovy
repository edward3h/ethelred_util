package org.ethelred.util.picocli.defaults

import io.github.cdimascio.dotenv.Dotenv
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import spock.lang.Specification

class EnvironmentDefaultValueProviderIntegrationSpec extends Specification {

    @Command(name = "myapp")
    static class MyCmd implements Runnable {
        @Option(names = "--host")
        String host

        @Option(names = "--port", defaultValue = "80")
        String port

        @Option(names = "--name")
        @EnvironmentDefaultValueProvider.Env("APP_NAME")
        String name

        void run() {}
    }

    def "env var provides default for option via PropertyKey"() {
        given:
        def dotenv = Stub(Dotenv) {
            get("MYAPP_HOST") >> "db.example.com"
            get(_) >> null
        }
        def cmd = new MyCmd()

        when:
        new CommandLine(cmd)
            .setDefaultValueProvider(new EnvironmentDefaultValueProvider(dotenv))
            .parseArgs()

        then:
        cmd.host == "db.example.com"
    }

    def "env var provides default via Env annotation"() {
        given:
        def dotenv = Stub(Dotenv) {
            get("APP_NAME") >> "myservice"
            get(_) >> null
        }
        def cmd = new MyCmd()

        when:
        new CommandLine(cmd)
            .setDefaultValueProvider(new EnvironmentDefaultValueProvider(dotenv))
            .parseArgs()

        then:
        cmd.name == "myservice"
    }

    def "explicit command line arg overrides env default"() {
        given:
        def dotenv = Stub(Dotenv) {
            get("MYAPP_HOST") >> "from-env"
            get(_) >> null
        }
        def cmd = new MyCmd()

        when:
        new CommandLine(cmd)
            .setDefaultValueProvider(new EnvironmentDefaultValueProvider(dotenv))
            .parseArgs("--host", "from-args")

        then:
        cmd.host == "from-args"
    }

    @Command(name = "myapp", defaultValueProvider = EnvironmentDefaultValueProvider)
    static class AnnotatedCmd implements Runnable {
        @Option(names = "--host")
        String host
        void run() {}
    }

    def "Dotenv can be injected via IFactory when provider is declared in annotation"() {
        given:
        def dotenv = Stub(Dotenv) {
            get("MYAPP_HOST") >> "injected"
            get(_) >> null
        }
        def factory = { Class cls ->
            cls == EnvironmentDefaultValueProvider
                ? new EnvironmentDefaultValueProvider(dotenv)
                : CommandLine.defaultFactory().create(cls)
        } as CommandLine.IFactory
        def cmd = new AnnotatedCmd()

        when:
        new CommandLine(cmd, factory).parseArgs()

        then:
        cmd.host == "injected"
    }

    def "annotation default is preserved when no env var matches"() {
        given:
        def dotenv = Stub(Dotenv) {
            get(_) >> null
        }
        def cmd = new MyCmd()

        when:
        new CommandLine(cmd)
            .setDefaultValueProvider(new EnvironmentDefaultValueProvider(dotenv))
            .parseArgs()

        then:
        cmd.port == "80"
    }
}
