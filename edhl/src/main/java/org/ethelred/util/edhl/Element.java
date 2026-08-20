/* (C) 2026 */
package org.ethelred.util.edhl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Element implements Consumer<Appendable> {
    final String name;
    private final Map<String, String> attributes = new HashMap<>();
    private final List<Element> children;
    private final boolean isVoid;

    public Element(String name, boolean isVoid) {
        this(name, List.of(), isVoid);
    }

    public Element(String name, List<Element> children) {
        this(name, children, false);
    }

    private Element(String name, List<Element> children, boolean isVoid) {
        this.name = name;
        this.children = children;
        this.isVoid = isVoid;
    }

    public Element classes(String... names) {
        attributes.merge("class", String.join(" ", names), (a, b) -> a + " " + b);
        return this;
    }

    public Element attr(String name, String value) {
        attributes.put(name, value);
        return this;
    }

    @Override
    public void accept(Appendable appendable) {
        try {
            appendable.append('<').append(name);
            attributes.forEach((key, value) -> {
                try {
                    appendable.append(String.format(" %s=\"%s\"", key, escapeAttributeValue(value)));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            appendable.append('>');
            children.forEach(element -> element.accept(appendable));
            if (!isVoid) {
                appendable.append("</").append(name).append('>');
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String toString() {
        var writer = new StringWriter();
        accept(writer);
        return writer.toString();
    }

    public void writeTo(Writer writer) {
        accept(writer);
        try {
            writer.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void writeTo(OutputStream outputStream) {
        writeTo(new OutputStreamWriter(outputStream));
    }

    private static String escapeAttributeValue(String value) {
        return value.replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
