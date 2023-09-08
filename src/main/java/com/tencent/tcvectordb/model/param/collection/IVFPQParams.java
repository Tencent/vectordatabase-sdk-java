package com.tencent.tcvectordb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IVFPQParams implements ParamsSerializer {
    private int nlist;
    @JsonProperty(value = "M")
    private int M;


    public IVFPQParams(int nlist, int m) {
        this.nlist = nlist;
        M = m;
    }

    public int getNlist() {
        return nlist;
    }

    public int getM() {
        return M;
    }

    @Override
    public String toString() {
        return "IVFPQParams{" +
                "nlist=" + nlist +
                ", M=" + M +
                '}';
    }
}
