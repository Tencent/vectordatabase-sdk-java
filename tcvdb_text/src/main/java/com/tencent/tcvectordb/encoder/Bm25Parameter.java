package com.tencent.tcvectordb.encoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Bm25Parameter {
    private Double k1;
    private Double b;
    @JsonProperty("token_freq")
    private Map<String, Integer> tokenFreq;
    @JsonProperty("doc_count")
    private Integer docCount;
    @JsonProperty("average_doc_length")
    private Double averageDocLength;
    @JsonProperty("stop_words")
    private Boolean  stopWords;
    @JsonProperty("lower_case")
    private Boolean  lowerCase;
    @JsonProperty("dict_file")
    private String  dictFile;

}
