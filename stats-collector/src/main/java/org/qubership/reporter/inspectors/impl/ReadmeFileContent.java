package org.qubership.reporter.inspectors.impl;

import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.InspectorResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ReadmeFileContent extends ARepositoryInspector {
    @Override
    protected InspectorResult inspectRepoFolder(String pathToRepository) throws Exception {
        File theFile = Paths.get(pathToRepository, "README.md").toFile();
        if (!theFile.exists()) return createError("Not found");
        if (!theFile.isFile()) return createError("Not found");

        Stream<String> lines = Files.lines(theFile.toPath());
        if (lines.count() < 5) return createError("Empty content");


        return ok("");
    }

    @Override
    protected String getMetricName() {
        return "README.md";
    }
}
