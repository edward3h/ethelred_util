package org.ethelred.util.picocli.defaults

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import spock.lang.Specification

class PropertyKeySpec extends Specification {

    @Command(name = "myapp")
    static class SimpleCmd {
        @Option(names = "--some-flag")
        String someFlag

        @Option(names = ["--verbose", "-v"])
        boolean verbose

        @Option(names = "--custom", descriptionKey = "my.custom.key")
        String custom

        @Parameters
        String positional
    }

    @Command(name = "myapp")
    static class ParentCmd {}

    @Command(name = "sub")
    static class SubCmd {
        @Option(names = "--option")
        String option
    }

    def "dashes in option name become underscores in env var"() {
        given:
        def spec = CommandLine.Model.CommandSpec.forAnnotatedObject(new SimpleCmd())
        def argSpec = spec.findOption("--some-flag")

        when:
        def key = PropertyKey.from(argSpec)

        then:
        key != null
        key.asEnvironmentVariable() == "MYAPP_SOME_FLAG"
    }

    def "longest name is used when option has multiple names"() {
        given:
        def spec = CommandLine.Model.CommandSpec.forAnnotatedObject(new SimpleCmd())
        def argSpec = spec.findOption("--verbose")

        when:
        def key = PropertyKey.from(argSpec)

        then:
        key != null
        key.asEnvironmentVariable() == "MYAPP_VERBOSE"
    }

    def "descriptionKey takes precedence over option name"() {
        given:
        def spec = CommandLine.Model.CommandSpec.forAnnotatedObject(new SimpleCmd())
        def argSpec = spec.findOption("--custom")

        when:
        def key = PropertyKey.from(argSpec)

        then:
        key != null
        key.asEnvironmentVariable() == "MYAPP_MY_CUSTOM_KEY"
    }

    def "positional parameter without descriptionKey returns null"() {
        given:
        def spec = CommandLine.Model.CommandSpec.forAnnotatedObject(new SimpleCmd())
        def argSpec = spec.positionalParameters().first()

        when:
        def key = PropertyKey.from(argSpec)

        then:
        key == null
    }

    def "subcommand option includes full command path"() {
        given:
        def parent = new CommandLine(new ParentCmd())
        parent.addSubcommand("sub", new SubCmd())
        def subSpec = parent.subcommands["sub"].commandSpec
        def argSpec = subSpec.findOption("--option")

        when:
        def key = PropertyKey.from(argSpec)

        then:
        key != null
        key.asEnvironmentVariable() == "MYAPP_SUB_OPTION"
    }
}
