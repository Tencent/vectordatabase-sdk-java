package com.tencent.tcvectordb.model.param.entity;

import com.tencent.tcvectordb.utils.JsonUtils;

import java.util.List;
import java.util.Map;

public class AtomicEmbeddingRes extends BaseRes{
    private Long tokenUsed;
    private List<List<Double>> denseVector;

    private List<Map<String, Double>> sparseVector;

    public Long getTokenUsed() {
        return tokenUsed;
    }

    public void setTokenUsed(Long tokenUsed) {
        this.tokenUsed = tokenUsed;
    }

    public List<List<Double>> getDenseVector() {
        return denseVector;
    }

    public void setDenseVector(List<List<Double>> denseVector) {
        this.denseVector = denseVector;
    }

    public List<Map<String, Double>> getSparseVector() {
        return sparseVector;
    }

    public void setSparseVector(List<Map<String, Double>> sparseVector) {
        this.sparseVector = sparseVector;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}
