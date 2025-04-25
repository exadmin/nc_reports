package org.qubership.reporter.inspectors.impl.cncf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Model {
    @JsonProperty("qubership-name")
    private String qubershipName;

    @JsonProperty("category")
    private String category;

    @JsonProperty("subcategory")
    private String subCategory;

    @JsonProperty("alternatives")
    private List<String> alternatives;

    @JsonProperty("reasons")
    private String reasons;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<String> alternatives) {
        this.alternatives = alternatives;
    }

    public String getQubershipName() {
        return qubershipName;
    }

    public void setQubershipName(String qubershipName) {
        this.qubershipName = qubershipName;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }
}