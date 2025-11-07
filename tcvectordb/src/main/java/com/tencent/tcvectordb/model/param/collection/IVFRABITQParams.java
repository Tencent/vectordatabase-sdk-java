package com.tencent.tcvectordb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IVFRABITQParams implements ParamsSerializer {
    @JsonProperty(value = "nlist")
    private int NList;
    @JsonProperty(value = "bits")
    private int bits;

    public IVFRABITQParams() {
    }

    public IVFRABITQParams(int NList, int bits) {
        this.NList = NList;
        this.bits = bits;
    }

    public int getNList() {
        return NList;
    }

    public int getBits() {
        return bits;
    }

    @Override
    public String toString() {
        return "IVFRABITQParams{" +
                "NList=" + NList +
                ", bits=" + bits +
                '}';
    }
}
