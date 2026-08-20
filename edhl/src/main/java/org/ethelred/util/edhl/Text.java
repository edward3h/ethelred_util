/* (C) 2026 */
package org.ethelred.util.edhl;

import java.io.IOException;
import java.io.UncheckedIOException;

public class Text extends Element {
    private final String text;

    public Text(String text) {
        super("", false);
        this.text = text;
    }

    @Override
    public void accept(Appendable appendable) {
        try {
            appendable.append(text);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Text[] textArray(String... text) {
        var result = new Text[text.length];
        for (int i = 0; i < text.length; i++) {
            result[i] = new Text(text[i]);
        }
        return result;
    }
}
