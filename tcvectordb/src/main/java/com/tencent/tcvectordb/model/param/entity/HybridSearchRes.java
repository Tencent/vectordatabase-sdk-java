package com.tencent.tcvectordb.model.param.entity;

import com.tencent.tcvectordb.exception.VectorDBException;
import com.tencent.tcvectordb.model.Document;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HybridSearchRes extends BaseRes {
    private List<Object> documents;

    public List<Document> getDocuments() throws VectorDBException{
        if (documents.size()==0){
            return Collections.emptyList();
        }
        if (documents.get(0) instanceof Document) {
            return documents.stream().map(o -> (Document) o).collect(Collectors.toList());
        }
        throw new VectorDBException("hybrid search response data type is List<List<Document>>, please use method: getDocumentsList");
    }

    public List<List<Document>> getDocumentsList() throws VectorDBException {
        if (documents.size()==0){
            return Collections.emptyList();
        }
        if (documents.get(0) instanceof List){
            return documents.stream().map(array -> (List<Document>) array).collect(Collectors.toList());
        }
        throw  new VectorDBException("hybrid search response data type is List<Document>, please use method: getDocuments");
    }

    public HybridSearchRes(int code, String msg, String warning, List<Object> documents) {
        super(code, msg, warning);
        this.documents = documents;
    }


    @Override
    public String toString() {
        return "HybridSearchRes{" +
                "documents=" + documents +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", warning='" + warning + '\'' +
                '}';
    }
}
