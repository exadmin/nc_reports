package org.qubership.reporter.inspectors.impl.postinstpectors.summary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.qubership.reporter.inspectors.api.AbstractPostInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.utils.DateUtils;
import org.qubership.reporter.utils.MiscUtils;
import org.qubership.reporter.utils.TheLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ErrorsDeltaInspector extends AbstractPostInspector {
    private static final Pattern REG_EXP = Pattern.compile("\\bpersisted-data-(\\d\\d-\\d\\d-\\d\\d\\d\\d-\\d\\d-\\d\\d-\\d\\d)\\.json");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");

    private static Map<String, String> oldMap;


    @Override
    public Metric getMetric() {
        return new Metric("Errors Count (Week Ago)", "Errors Count (Week Ago)", MetricGroupsRegistry.SYSTEM_METRIC_GROUP)
                .setDescription(""); // todo
    }

    @Override
    public void doPostInspection(ReportModel reportModel, List<Map<String, Object>> allReposMetaData) {

    }

    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        // load persisted data from previous reports
        if (oldMap == null) {
            oldMap = Collections.emptyMap(); // if no assigment will be done later - the oldMap will never be null any way.

            // list existed reports
            Path startPath = Paths.get(pathToRepository + "/../data");
            TheLogger.debug("Looking for previous reports data at " + startPath);
            List<Path> reportFiles = Collections.emptyList();
            try (Stream<Path> files = Files.list(startPath)) {
                reportFiles = files.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".json")).collect(Collectors.toList());
            } catch (IOException ioex) {
                TheLogger.error("Error while loading prev reports data", ioex);
                oldMap = new HashMap<>();
            }

            TheLogger.debug("Found " + reportFiles.size() + " *.json files");

            // create map of file-name to date
            Map<String, String> name2DateMap = new HashMap<>();

            for (Path next : reportFiles) {
                String shortFileName = next.getFileName().toString();
                Matcher matcher = REG_EXP.matcher(shortFileName);
                if (matcher.find()) {
                    String dateStr = matcher.group(1);
                    name2DateMap.put(shortFileName, dateStr);
                }
            }

            // here we shoold have non empty map
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime weekAgo = currentDateTime.minusWeeks(1);
            final Date targetDate = DateUtils.toDate(weekAgo.format(DATE_FORMATTER));

            // todo: compare map with dateAsStr
            String selectedFileBefore = null;
            String selectedFileAfter = null;

            long timeDeltaForFileBefore = Long.MAX_VALUE;
            long timeDeltaForFileAfter = Long.MAX_VALUE;


            for (Map.Entry<String, String> me : name2DateMap.entrySet()) {
                String fileName = me.getKey();
                String fileDateStr = me.getValue();
                Date fileDate = DateUtils.toDate(fileDateStr);

                long timeDelta = Math.abs(fileDate.getTime() - targetDate.getTime());

                if (fileDate.before(targetDate)) {
                    if (timeDelta < timeDeltaForFileBefore) {
                        selectedFileBefore = fileName;
                        timeDeltaForFileBefore = timeDelta;
                    }
                } else {
                    if (timeDelta < timeDeltaForFileAfter) {
                        selectedFileAfter = fileName;
                        timeDeltaForFileAfter = timeDelta;
                    }
                }
            }

            TheLogger.debug("Selecting " + selectedFileBefore + " as file-before requested date");
            TheLogger.debug("Selecting " + selectedFileAfter + " as file-after requested date");

            String selectedFile = selectedFileBefore;

            // if there were no files a week ago - select oldest one
            if (selectedFileBefore == null) selectedFile = selectedFileAfter;

            // load file which was selected as
            if (selectedFile != null) {
                try {
                    TypeReference<Map<String, String>> typeRef = new TypeReference<>() {
                    };

                    ObjectMapper mapper = new ObjectMapper(new JsonFactory());
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                    oldMap = mapper.readValue(new File(startPath + File.separator + selectedFile), typeRef);
                } catch (Exception ex) {
                    TheLogger.error("Error while loading json format from " + selectedFile, ex);
                    oldMap = new HashMap<>();
                }
            }
        }

        if (!oldMap.isEmpty()) {
            File repoDir = new File(pathToRepository);
            String fatKey = MiscUtils.createComplexKey(repoDir.getName(), getMetric().getPersistenceId());

            String oldValue = oldMap.get(fatKey);
            if (oldValue != null) {
                return info(oldValue);
            }
        }

        return info("No data");
    }
}
