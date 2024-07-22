package com.tencent.tcvdb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IVFFLATParams implements ParamsSerializer {
    @JsonProperty(value = "nlist")
    private int NList;

    public IVFFLATParams() {
    }

    public IVFFLATParams(int NList) {
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
