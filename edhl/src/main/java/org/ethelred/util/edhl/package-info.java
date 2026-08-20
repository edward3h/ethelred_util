/**
 * Edward's Dumb HTML Library
 * <p>
 * A simple way to generate some HTML from Java without needing an extra build step.
 * My use case is in a JBang script.</p>
 * <p>It is designed to use a static import.</p>
 * <pre>{@code
 * import static org.ethelred.util.edhl.Html.*;
 * }</pre>
 * <p>Then write HTML structure</p>
 * <pre>{@code
 * var myDoc =
 * html(
 *      head(title("My Doc")),
 *      body(
 *          h1("Heading goes here"),
 *          p(
 *              text("Some text "),
 *              strong("then some strong text"),
 *              text(" then more text")
 *          ),
 *          div("Text content").classes("some", "classes"),
 *          div(
 *              a("A link").attr("href", "http://www.example.com")
 *          )
 *
 *      )
 * );
 *
 * myDoc.writeTo(System.out);
 * }</pre>
 */
@NullMarked
package org.ethelred.util.edhl;

import org.jspecify.annotations.NullMarked;
