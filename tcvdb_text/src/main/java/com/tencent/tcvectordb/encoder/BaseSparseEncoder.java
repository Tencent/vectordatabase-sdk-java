package com.tencent.tcvectordb.encoder;


import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
public interface BaseSparseEncoder {
    List<List<Pair<Long, Float>>> encodeTexts(List<String> texts);
    List<List<Pair<Long, Float>>> encodeQueries(List<String> texts);
    void fitCorpus(List<String> texts);
    Bm25Parameter downloadParams(String paramsFile);
    void setParams(String paramsFile);
    void setDict(String dictFile);

}
