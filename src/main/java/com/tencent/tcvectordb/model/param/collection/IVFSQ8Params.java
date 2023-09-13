package com.tencent.tcvectordb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IVFSQ8Params implements ParamsSerializer {
    @JsonProperty(value = "nlist")
    private int NList;

    public IVFSQ8Params() {
    }

    public IVFSQ8Params(int NList) {
        this.NList = NList;
    }

    public int getNList() {
        return NList;
    }

    @Override
    public String toString() {
        return "IVFFLATParams{" +
                "NList=" + NList +
                '}';
    }
}
