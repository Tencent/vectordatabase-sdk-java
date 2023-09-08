package com.tencent.tcvectordb.model.param.collection;

public class IVFSQ8Params implements ParamsSerializer {
    private int nlist;

    public IVFSQ8Params(int nlist) {
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
