package com.tencent.tcvectordb.encoder;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class Bm25Encoder implements BaseSparseEncoder{
    @Override
    public List<List<Pair<Integer, Double>>> encodeTexts(List<String> texts) {
        return null;
    }

    @Override
    public List<List<Pair<Integer, Double>>> encodeQueries(List<String> texts) {
        return null;
    }

    @Override
    public void fitCorpus(List<String> texts) {

    }

    @Override
    public void downloadParams(String paramsFile) {

    }

    @Override
    public void setParams(String paramsFile) {

    }

    @Override
    public void setDict(String dictFile) {

    }
}
