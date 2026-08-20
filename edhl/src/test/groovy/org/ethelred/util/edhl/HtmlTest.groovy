package org.ethelred.util.edhl

import spock.lang.Specification
import static org.ethelred.util.edhl.Html.*;

class HtmlTest extends Specification {
    def "Hello World page renders as expected"() {
        when:
        def page = html(head(title("Hello world")), body(h1("Hello world")))

        then:
        page.toString() == "<html><head><title>Hello world</title></head><body><h1>Hello world</h1></body></html>"
    }

    def "attribute values are HTML-escaped"() {
        when:
        def element = div("content").attr("title", 'a "quote" & <tag>')

        then:
        element.toString() == '<div title="a &quot;quote&quot; &amp; &lt;tag&gt;">content</div>'
    }
}
