package org.qubership.reporter.inspectors.impl;

import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.InspectorResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ReadmeFileContent extends ARepositoryInspector {
    @Override
    protected InspectorResult inspectRepoFolder(String pathToRepository, List<Map<String, Object>> metaData) throws Exception {
        File theFile = Paths.get(pathToRepository, "README.md").toFile();
        if (!theFile.exists()) return error("Not found");
        if (!theFile.isFile()) return error("Not found");

        Stream<String> lines = Files.lines(theFile.toPath());
        if (lines.count() < 5) return error("Empty content");


        return ok("");
    }

    @Override
    protected String getMetricName() {
        return "README.md";
    }

    @Override
    protected String getMetricDescriptionInMDFormat() {
        return "Checks if '/README.md' file exist in the repository and contains not less then 5 lines.";
    }
}
