package org.qubership.reporter.utils;

import java.util.Map;

public class RepoUtils {
    public static String getRepositoryName(Map<String, Object> repoMetaData) {
        return (String) repoMetaData.get("name");
    }

    public static String getReferenceToFileInGitHub(Map<String, Object> repoMetaData, String filePath) {
        String defBranch = (String) repoMetaData.get("default_branch");
        String url = (String) repoMetaData.get("html_url");
        return url + "/blob/" + defBranch + "/" + filePath;
    }

    public static String getPullsURL(Map<String, Object> repoMetaData) {
        return (String) repoMetaData.get("pulls_url");
    }
}
