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
