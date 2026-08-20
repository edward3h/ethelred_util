/* (C) 2026 */
package org.ethelred.util.edhl;

import java.util.List;

public class __template__ {
    private __template__() {}

    public static Text text(String text) {
        return new Text(text);
    }

    public static Element tag(String name, Element... children) {
        return new Element(name, List.of(children));
    }

    public static Element tag(String name, List<Element> children) {
        return new Element(name, children);
    }

    public static Element voidTag(String name) {
        return new Element(name, true);
    }

    // start template tagnames
    public static Element __tagname__(Element... children) {
        return new Element("__tagname__", List.of(children));
    }

    public static Element __tagname__(List<Element> children) {
        return new Element("__tagname__", children);
    }

    public static Element __tagname__(String text) {
        return new Element("__tagname__", List.of(text(text)));
    }

    // end template tagnames

    // start template voidtagnames
    public static Element __tagname__() {
        return new Element("__tagname__", true);
    }

    // end template voidtagnames
}
