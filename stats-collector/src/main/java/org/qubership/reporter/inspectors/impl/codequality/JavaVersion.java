package org.qubership.reporter.inspectors.impl.codequality;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.inspectors.api.TextAlign;
import org.qubership.reporter.model.MetricGroup;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class JavaVersion extends ARepositoryInspector {
    private static final String SUFFIX = File.separator + "pom.xml";
    private static final XmlMapper xmlMapper = new XmlMapper();

    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        List<String> foundPoms = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(Paths.get(pathToRepository))) {
            stream
                    .filter(Files::isRegularFile)
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .filter(s -> s.endsWith(SUFFIX))
                    .forEach(foundPoms::add);
        }

        for (String fileName : foundPoms) {
            TypeReference<Map<String, Object>> type = new TypeReference<>() {};

            Map<String, Object> obj = xmlMapper.readValue(new FileInputStream(fileName), type);
            String compilerRelease = getStringValue(obj, "maven.compiler.release");
            String compilerTarget  = getStringValue(obj, "maven.compiler.target");

            if (compilerRelease != null) return genResult(compilerRelease);
            if (compilerTarget != null) return genResult(compilerTarget);
        }

        // there were poms but no info was found
        if (!foundPoms.isEmpty()) {
            return error("Not specified");
        }

        // seems it's not java-project
        return info("");
    }

    private OneMetricResult genResult(String msg) {
        OneMetricResult res = info(msg);
        res.setTextAlign(TextAlign.CENTER_MIDDLE);
        return res;
    }

    @Override
    public String getMetricName() {
        return "JavaVersion";
    }

    @Override
    protected String getMetricDescriptionInMDFormat() {
        return "Scans all pom.xml files and collects java.version property values";
    }

    private static String getStringValue(Map<String, Object> xmlMap, String propertyNameInPropertiesSection) {
        Map<String, Object> propertiesXmlMap = (Map<String, Object>) xmlMap.get("properties");
        if (propertiesXmlMap != null) {
            Object propertyValue = propertiesXmlMap.get(propertyNameInPropertiesSection);
            if (propertyValue != null) return propertyValue.toString();
        }

        return null;
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.CODE_QUALITY_GROUP;
    }
}
