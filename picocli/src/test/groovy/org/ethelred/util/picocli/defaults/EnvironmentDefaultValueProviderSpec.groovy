package org.ethelred.util.picocli.defaults

import io.github.cdimascio.dotenv.Dotenv
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import spock.lang.Specification

class EnvironmentDefaultValueProviderSpec extends Specification {

    @Command(name = "myapp")
    static class CmdWithEnv {
        @Option(names = "--host")
        @EnvironmentDefaultValueProvider.Env("MY_HOST")
        String host

        @Option(names = "--port")
        @EnvironmentDefaultValueProvider.Env(["PORT_A", "PORT_B"])
        String port

        @Option(names = "--name")
        String name
    }

    def commandSpec() {
        CommandLine.Model.CommandSpec.forAnnotatedObject(new CmdWithEnv())
    }

    def "Env annotation - found env var returns its value"() {
        given:
        def dotenv = Stub(Dotenv) {
            get("MY_HOST") >> "localhost"
            get(_) >> null
        }
        def provider = new EnvironmentDefaultValueProvider(dotenv)
        def argSpec = commandSpec().findOption("--host")

        when:
        def value = provider.defaultValue(argSpec)

        then:
        value == "localhost"
    }

    def "Env annotation - multiple keys tried in order, first non-blank wins"() {
        given:
        def dotenv = Stub(Dotenv) {
            get("PORT_A") >> null
            get("PORT_B") >> "8080"
        }
        def provider = new EnvironmentDefaultValueProvider(dotenv)
        def argSpec = commandSpec().findOption("--port")

        when:
        def value = provider.defaultValue(argSpec)

        then:
        value == "8080"
    }

    def "Env annotation - blank value is skipped and next key is tried"() {
        given:
        def dotenv = Stub(Dotenv) {
            get("PORT_A") >> "   "
            get("PORT_B") >> "9090"
        }
        def provider = new EnvironmentDefaultValueProvider(dotenv)
        def argSpec = commandSpec().findOption("--port")

        when:
        def value = provider.defaultValue(argSpec)

        then:
        value == "9090"
    }

    def "Env annotation present suppresses PropertyKey fallback"() {
        given:
        def dotenv = Stub(Dotenv) {
            get("MY_HOST") >> null
            get("MYAPP_HOST") >> "should-not-be-returned"
        }
        def provider = new EnvironmentDefaultValueProvider(dotenv)
        def argSpec = commandSpec().findOption("--host")

        when:
        def value = provider.defaultValue(argSpec)

        then:
        value == null
    }

    def "no Env annotation falls back to PropertyKey"() {
        given:
        def dotenv = Stub(Dotenv) {
            get("MYAPP_NAME") >> "alice"
            get(_) >> null
        }
        def provider = new EnvironmentDefaultValueProvider(dotenv)
        def argSpec = commandSpec().findOption("--name")

        when:
        def value = provider.defaultValue(argSpec)

        then:
        value == "alice"
    }

    def "no Env annotation and no matching env var returns null"() {
        given:
        def dotenv = Stub(Dotenv) {
            get(_) >> null
        }
        def provider = new EnvironmentDefaultValueProvider(dotenv)
        def argSpec = commandSpec().findOption("--name")

        when:
        def value = provider.defaultValue(argSpec)

        then:
        value == null
    }
}
