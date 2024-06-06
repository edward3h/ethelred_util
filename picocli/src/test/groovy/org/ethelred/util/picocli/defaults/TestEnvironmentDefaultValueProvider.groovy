package org.ethelred.util.picocli.defaults

import spock.lang.Specification
import picocli.CommandLine

/**
*/
class TestEnvironmentDefaultValueProvider extends Specification {
    Map<String, String> env = [:]

    def setup() {
        EnvironmentDefaultValueProvider.envLoader = env::get
    }

    def 'value from option'() {
        when:
        def app = new FakeApp()
        new CommandLine(app).execute("--my-option", "vfo")

        then:
        app.myOption == "vfo"
    }

    def 'value from env'() {
        when:
            env['F_MY_OPTION'] = 'vfe'
            def app = new FakeApp()
            new CommandLine(app).execute()

        then:
            app.myOption == 'vfe'
    }

    def 'no value'() {
        when:
            def app = new FakeApp()
            new CommandLine(app).execute()

        then:
            app.myOption == 'fake_default'

    }

    @CommandLine.Command(name = "f", defaultValueProvider = EnvironmentDefaultValueProvider)
    static class FakeApp implements Runnable{
        @CommandLine.Option(names = ["--my-option"], defaultValue = "fake_default")
        String myOption

        void run() {
            // do nothing
        }
    }
}
