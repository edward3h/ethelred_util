/* (C) 2026 */
package org.ethelred.util.io;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class MoreFiles {
    private MoreFiles() {}

    public static Stream<Path> walkIntoZip(Path path, FileVisitOption... options) throws IOException {
        return Files.walk(path, options).flatMap(p -> expandZip(p, options));
    }

    private static Stream<Path> expandZip(Path path, FileVisitOption... options) {
        if (path.toString().endsWith(".zip")) {
            try {
                var fs = FileSystems.newFileSystem(path, null);
                return Files.walk(fs.getPath("/"), options);
            } catch (IOException ignore) {
            }
        }
        return Stream.of(path);
    }
}
