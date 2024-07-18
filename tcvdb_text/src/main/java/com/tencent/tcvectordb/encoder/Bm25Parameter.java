package com.tencent.tcvectordb.encoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Bm25Parameter {
    private Double k1;
    private Double b;
    private Map<Integer, Integer> tokenFreq;
    private Integer docCount;
    private Double averageDocLength;
    private Boolean  stopWords;
    private Boolean  lowerCase;
    private String  dictFile;

}
