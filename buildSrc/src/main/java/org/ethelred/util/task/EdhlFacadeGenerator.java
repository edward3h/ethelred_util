/* (C) 2026 */
package org.ethelred.util.task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

public abstract class EdhlFacadeGenerator extends DefaultTask {
    @InputFile
    public abstract RegularFileProperty getTags();

    @InputFile
    public abstract RegularFileProperty getTemplate();

    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    enum ReadState {
        header,
        template,
        footer
    }

    static class Tag {
        final String name;
        final boolean isVoid;

        Tag(String name) {
            var splits = name.split(",");
            this.name = splits[0].trim();
            this.isVoid = splits.length == 2 && splits[1].trim().equals("void");
        }
    }

    @TaskAction
    public void execute() throws IOException {
        var templatePath = getTemplate().get().getAsFile().toPath();
        var tagsPath = getTags().get().getAsFile().toPath();

        List<Tag> tags;
        try (var lines = Files.lines(tagsPath)) {
            tags = lines.map(String::strip)
                    .filter(line -> !line.isEmpty())
                    .map(Tag::new)
                    .collect(Collectors.toList());
        }
        var templateLines = Files.readAllLines(templatePath);
        String className = "Html";
        var headerLines = new ArrayList<String>();
        var templates = new HashMap<String, List<String>>();
        var footerLines = new ArrayList<String>();
        var state = ReadState.header;
        var packageName = "org.ethelred.util.edhl";
        var templateName = "";
        for (var line : templateLines) {
            if (line.contains("__remove__")) {
                continue;
            }
            if (line.contains("start template")) {
                templateName = line.substring(line.indexOf("start template ") + "start template ".length())
                        .strip();
                state = ReadState.template;
                continue;
            }
            if (line.contains("end template")) {
                state = ReadState.footer;
                continue;
            }
            line = line.replace("__template__", className);
            switch (state) {
                case header:
                    headerLines.add(line);
                    break;
                case template:
                    templates
                            .computeIfAbsent(templateName, ignore -> new ArrayList<>())
                            .add(line);
                    break;
                case footer:
                    footerLines.add(line);
                    break;
            }
        }
        var fullDir = getOutputDirectory().getAsFile().get().toPath().resolve(packageName.replace(".", "/"));
        Files.createDirectories(fullDir);
        try (var writer = Files.newBufferedWriter(fullDir.resolve(className + ".java"), StandardCharsets.UTF_8)) {
            for (var line : headerLines) {
                writer.write(line);
                writer.newLine();
            }
            for (var tag : tags) {
                var template = templates.get(tag.isVoid ? "voidtagnames" : "tagnames");
                if (template == null) {
                    throw new IllegalStateException(templates.keySet().toString());
                }
                for (var line : template) {
                    writer.write(line.replace("__tagname__", tag.name));
                    writer.newLine();
                }
            }
            for (var line : footerLines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
