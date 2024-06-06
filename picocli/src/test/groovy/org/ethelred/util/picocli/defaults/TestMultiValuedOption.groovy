package org.ethelred.util.picocli.defaults

import picocli.CommandLine
import spock.lang.Specification

/**
*/
class TestMultiValuedOption extends Specification {
    def 'no value'() {
        when:
        def app = new FakeApp()
        new CommandLine(app).execute()

        then:
        app.myOption == ['fake', 'bogus']
    }

    def 'single value'() {
        when:
            def app = new FakeApp()
            new CommandLine(app).execute("--list", "hello")

        then:
            app.myOption == ['hello']
    }

    def 'multi values'() {
        when:
            def app = new FakeApp()
            new CommandLine(app).execute("--list", "hello", "--list", "world")

        then:
            app.myOption == ['hello', 'world']
    }

    @CommandLine.Command(name = "f", defaultValueProvider = FakeDefaultProvider)
    static class FakeApp implements Runnable {
        @CommandLine.Option(names = ["--list"],split = ",")
        List<String> myOption

        void run() {
            // do nothing
        }
    }

    static class FakeDefaultProvider implements CommandLine.IDefaultValueProvider {

        @Override
        String defaultValue(CommandLine.Model.ArgSpec argSpec) throws Exception {
            'fake,bogus'
        }
    }
}
