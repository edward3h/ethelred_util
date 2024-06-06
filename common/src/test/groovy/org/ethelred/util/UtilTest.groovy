package org.ethelred.util

import spock.lang.Specification
import spock.lang.TempDir
import spock.util.io.FileSystemFixture

import java.nio.file.Path

class UtilTest extends Specification {
    @TempDir
    FileSystemFixture fs

    def "empty directory has size 0"() {
        when:
        fs.create {
            dir('temp')
        }

        then:
        Util.size(fs.resolve('temp')) == 0
    }

    def "single file has size of the file (character)"() {
        given:
        fs.create {
            file("testSizeFile.txt")
        }

        when:
        writeTestFile(fs.resolve('testFileSize.txt'), 1234)

        then:
        Util.size(fs.resolve('testFileSize.txt')) == 1234
    }

    def "directory containing multiple files has size of the sum of the files"() {
        given:
        fs.create {
            dir('testDir') {
                file('a.txt')
                file('b.txt')
            }
        }

        when:
        writeTestFile(fs.resolve('testDir/a.txt'), 123)
        writeTestFile(fs.resolve('testDir/b.txt'), 456)

        then:
        Util.size(fs.resolve('testDir')) == 579
    }

    private static final String LETTERS = "QWERTYUIOP"

    def writeTestFile(Path path, int size) {
        Random r = new Random(path.hashCode())
        StringBuilder buf = new StringBuilder()
        (0..<size).each {
            buf.append(LETTERS[r.nextInt(LETTERS.length())])
        }
        path.write(buf.toString())
    }
}