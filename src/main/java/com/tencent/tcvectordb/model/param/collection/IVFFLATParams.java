package com.tencent.tcvectordb.model.param.collection;

public class IVFFLATParams implements ParamsSerializer {
    private int nlist;

    public IVFFLATParams(int nlist) {
        this.nlist = nlist;
    }

    public int getNlist() {
        return nlist;
    }

    @Override
    public String toString() {
        return "IVFFLATParams{" +
                "nlist=" + nlist +
                '}';
    }
}
