/* (C) 2025 */
package org.ethelred.util.task;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

public abstract class MarkdownTask extends DefaultTask {
    @InputDirectory
    public abstract DirectoryProperty getFrom();

    @OutputDirectory
    public abstract DirectoryProperty getInto();

    @Inject
    public MarkdownTask() {
        super();
        getInto().convention(getProject().getLayout().getBuildDirectory().dir("markdown"));
    }

    @TaskAction
    public void convert() throws IOException {

        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        var projectPath =
                getProject().getLayout().getProjectDirectory().getAsFile().toPath();
        var targetDirPath = getInto().getAsFile().get().toPath();
        for (var sourceFile : getFrom().getAsFileTree()) {
            var sourcePath = sourceFile.toPath();
            var targetPath = targetDirPath.resolve(projectPath.relativize(sourcePath));
            Files.createDirectories(targetPath.getParent());
            if (sourcePath.getFileName().toString().endsWith(".md")) {
                targetPath = targetPath
                        .getParent()
                        .resolve(sourcePath.getFileName().toString().replace(".md", ".html"));
                Node document = parser.parseReader(Files.newBufferedReader(sourcePath, StandardCharsets.UTF_8));
                try (var writer = Files.newBufferedWriter(targetPath, StandardCharsets.UTF_8)) {
                    renderer.render(document, writer);
                }
            } else {
                Files.copy(sourcePath, targetPath);
            }
        }
    }
}
