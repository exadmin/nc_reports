package org.qubership.reporter.inspectors.impl.cncf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ResultSeverity;
import org.qubership.reporter.inspectors.impl.cncf.model.Model;
import org.qubership.reporter.inspectors.impl.cncf.model.ModelsContainer;
import org.qubership.reporter.utils.RepoUtils;
import org.qubership.reporter.utils.TokenHolder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class CNCFCategorization extends AbstractRepositoryInspector {
    private static ModelsContainer modelsContainer; // todo: change this approach (at least for thread safety)

    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        return null;
    }

    @Override
    protected List<OneMetricResult> inspectRepoFolderWithManyMetrics(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        // load data from prepared file and render it
        if (modelsContainer == null) {
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            TypeReference<ModelsContainer> type = new TypeReference<>() {
            };

            String dsMapFilePath = Paths.get(TokenHolder.getDbFilePath()).getParent().toString() + "./../ds-map.json";
            modelsContainer = mapper.readValue(new File(dsMapFilePath), ModelsContainer.class);
        }

        String dsCategory = null;
        String dsSubCategory = null;
        String dsAlternatives = null;
        String dsNotes = null;

        String repoName = RepoUtils.getRepositoryName(repoMetaData);
        for (Model model : modelsContainer.getModels()) {
            if (model.getQubershipName().equals(repoName)) {
                dsCategory = model.getCategory();
                dsSubCategory = model.getSubCategory();
                dsAlternatives = listToStr(model.getAlternatives());
                dsNotes = model.getReasons();
            }
        }

        OneMetricResult omrCategory = new OneMetricResult(getCategoryMetric(), getSeverityByStr(dsCategory), getRawDataValue(dsCategory));
        omrCategory.setTitleText(getTitleTextByStr(dsCategory));

        OneMetricResult omrSubcategory = new OneMetricResult(getSubCategoryMetric(), getSeverityByStr(dsSubCategory), getRawDataValue(dsSubCategory));
        omrSubcategory.setTitleText(getTitleTextByStr(dsSubCategory));

        OneMetricResult omrAlternatives = new OneMetricResult(getAlternativesMetric(), getSeverityByStr(dsAlternatives), getRawDataValue(dsAlternatives));
        omrAlternatives.setTitleText(getTitleTextByStr(dsAlternatives));

        OneMetricResult omrNotes = new OneMetricResult(getNotes(), ResultSeverity.INFO, dsNotes);

        ResultSeverity overallStatusSeverity = (isNotOk(omrCategory.getSeverity())
            || isNotOk(omrSubcategory.getSeverity())
            || isNotOk(omrAlternatives.getSeverity())) ? ResultSeverity.ERROR : ResultSeverity.OK;

        OneMetricResult omrStatus = new OneMetricResult(getMetric(), overallStatusSeverity, "");
        if (ResultSeverity.ERROR.equals(overallStatusSeverity)) {
            omrStatus.setTitleText("There are errors. See other columns for details.");
        }

        return List.of(omrStatus, omrCategory, omrSubcategory, omrAlternatives, omrNotes);
    }

    @Override
    public Metric getMetric() {
        return newMetric("CNCF/Status", "CNCF Status", MetricGroupsRegistry.CNCF_GROUP)
                .setRenderingOrderWeight(-50)
                .setDescriptionRef("");
    }

    private Metric getCategoryMetric() {
        return newMetric("CNCF/Category", "CNCF Category", MetricGroupsRegistry.CNCF_GROUP)
                .setRenderingOrderWeight(-30)
                .setDescriptionRef("");
    }

    private Metric getSubCategoryMetric() {
        return newMetric("CNCF/Subcategory", "CNCF Subcategory", MetricGroupsRegistry.CNCF_GROUP)
                .setRenderingOrderWeight(-10)
                .setDescriptionRef("");
    }

    private Metric getAlternativesMetric() {
        return newMetric("CNCF/Alternatives", "GenAI Alternatives", MetricGroupsRegistry.CNCF_GROUP)
                .setRenderingOrderWeight(-1)
                .setDescriptionRef("");
    }

    private Metric getNotes() {
        return newMetric("CNCF/Notes", "GenAI Notes", MetricGroupsRegistry.CNCF_GROUP)
                .setRenderingOrderWeight(+1)
                .setDescriptionRef("");
    }

    private static ResultSeverity getSeverityByStr(String str) {
        if (str == null) return ResultSeverity.WARN;
        return (str.equalsIgnoreCase("n/a")) ? ResultSeverity.ERROR : ResultSeverity.INFO;
    }

    private static String getTitleTextByStr(String str) {
        if (str == null) return "Data is not prepared yet";
        if (str.equalsIgnoreCase("n/a")) return "Not enough data to make decision";
        return null;
    }

    private static String getRawDataValue(String str) {
        if (str == null) return "";
        return str;
    }

    private static String listToStr(List<String> list) {
        if (list == null) return "";

        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(str);
        }

        return sb.toString();
    }

    private static boolean isNotOk(ResultSeverity resultSeverity) {
        return ResultSeverity.ERROR.equals(resultSeverity) || ResultSeverity.WARN.equals(resultSeverity) || ResultSeverity.SECURITY_ISSUE.equals(resultSeverity);
    }
}
