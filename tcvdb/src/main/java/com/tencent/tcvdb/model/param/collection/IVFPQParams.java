package com.tencent.tcvdb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IVFPQParams implements ParamsSerializer {
    @JsonProperty(value = "nlist")
    private int NList;
    @JsonProperty(value = "M")
    private int M;

    public IVFPQParams() {
    }

    public IVFPQParams(int NList, int m) {
        this.NList = NList;
        M = m;
    }

    public int getNList() {
        return NList;
    }

    public int getM() {
        return M;
    }

    @Override
    public String toString() {
        return "IVFPQParams{" +
                "NList=" + NList +
                ", M=" + M +
                '}';
    }
}
