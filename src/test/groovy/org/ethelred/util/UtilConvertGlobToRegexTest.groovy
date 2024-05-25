import org.ethelred.util.Util
import spock.lang.Specification

class UtilConvertGlobToRegexTest extends Specification {
    def "glob #glob is converted to regex #expected"() {
        when:
        def regex = Util.convertGlobToRegex(glob)

        then:
        regex == expected

        where:
        glob               || expected
        'gl*b'             || 'gl.*b'
        'gl\\*b'           || 'gl\\*b'
        'gl?b'             || 'gl.b'
        'gl\\?b'           || 'gl\\?b'
        'gl[-o]b'          || 'gl[-o]b'
        'gl\\[-o\\]b'      || 'gl\\[-o\\]b'
        "gl[!a-n!p-z]b"    || "gl[^a-n!p-z]b"
        "gl[^o]b"          || "gl[\\^o]b"
        'gl?*.()+|^$@%b'   || 'gl..*\\.\\(\\)\\+\\|\\^\\$\\@\\%b'
        'gl[?*.()+|^$@%]b' || 'gl[?*.()+|^$@%]b'
        "gl\\\\b"          || "gl\\\\b"
        "{glob,regex}"     || "(glob|regex)"
        "\\{glob\\}"       || "\\{glob\\}"
        "{glob\\,regex},"  || "(glob,regex),"
    }
}