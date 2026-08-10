package org.ethelred.util.io

import spock.lang.Specification
import spock.lang.TempDir
import spock.util.io.FileSystemFixture

import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class MoreFilesTest extends Specification {
    @TempDir
    FileSystemFixture fs

    def "walkIntoZip behaves like a plain walk when there are no zip files"() {
        given:
        def root = fs.resolve('plain')
        Files.createDirectories(root.resolve('sub'))
        Files.createFile(root.resolve('a.txt'))
        Files.createFile(root.resolve('sub/b.txt'))

        when:
        def walked = MoreFiles.walkIntoZip(root).toList()

        then:
        walked as Set == Files.walk(root).toList() as Set
        walked.collect { it.fileName?.toString() }.containsAll(['plain', 'a.txt', 'sub', 'b.txt'])
    }

    def "walkIntoZip expands a zip file found within a directory"() {
        given:
        def root = fs.resolve('withZip')
        Files.createDirectories(root)
        Files.createFile(root.resolve('a.txt'))
        def zipPath = root.resolve('archive.zip')
        writeZip(zipPath, ['inner.txt': 'hello', 'nested/deep.txt': 'world'])

        when:
        def walked = MoreFiles.walkIntoZip(root).toList()
        def names = walked.collect { it.fileName?.toString() }

        then:
        // regular file is present as-is
        names.contains('a.txt')
        // the zip's own path is not present as a leaf, only its expanded contents
        !walked.any { it.toString() == zipPath.toString() }
        names.contains('inner.txt')
        names.contains('deep.txt')
    }

    def "walkIntoZip expands the contents when the zip itself is the root"() {
        given:
        def root = fs.resolve('zipRoot')
        Files.createDirectories(root)
        def zipPath = root.resolve('archive.zip')
        writeZip(zipPath, ['one.txt': 'a', 'two.txt': 'b'])

        when:
        def walked = MoreFiles.walkIntoZip(zipPath).toList()
        def names = walked.collect { it.fileName?.toString() }.findAll { it }

        then:
        names.containsAll(['one.txt', 'two.txt'])
    }

    def "walkIntoZip falls back to the original path for an invalid zip file"() {
        given:
        def root = fs.resolve('badZip')
        Files.createDirectories(root)
        def badZip = root.resolve('broken.zip')
        Files.writeString(badZip, "not actually a zip file")

        when:
        def walked = MoreFiles.walkIntoZip(badZip).toList()

        then:
        walked == [badZip]
    }

    private static void writeZip(Path path, Map<String, String> entries) {
        Files.createDirectories(path.parent)
        new ZipOutputStream(Files.newOutputStream(path)).withCloseable { zos ->
            entries.each { name, content ->
                zos.putNextEntry(new ZipEntry(name))
                zos.write(content.getBytes('UTF-8'))
                zos.closeEntry()
            }
        }
    }
}
