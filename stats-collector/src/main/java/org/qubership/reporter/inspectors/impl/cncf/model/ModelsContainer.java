package org.qubership.reporter.inspectors.impl.cncf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ModelsContainer {
    @JsonProperty("models")
    private List<Model> models;

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public void addModel(Model model) {
        if (models == null) models = new ArrayList<>();
        models.add(model);
    }
}
