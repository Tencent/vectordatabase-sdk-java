package com.tencent.tcvectordb.encoder;


import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
public interface BaseSparseEncoder {
    public List<List<Pair<Long, Float>>> encodeTexts(List<String> texts);
    public List<List<Pair<Long, Float>>> encodeQueries(List<String> texts);
    public void fitCorpus(List<String> texts);
    public Bm25Parameter downloadParams(String paramsFile);
    public void setParams(String paramsFile);
    public void setDict(String dictFile);

}
