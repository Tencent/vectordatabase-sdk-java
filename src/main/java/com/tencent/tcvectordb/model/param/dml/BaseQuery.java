package com.tencent.tcvectordb.model.param.dml;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseQuery {
    protected String filter;
    protected List<String> documentIds;

    public BaseQuery(Builder<?> builder) {
        if (builder.filter != null) {
            this.filter = builder.filter.getCond();
        }
        this.documentIds = builder.documentIds;
    }

    public String getFilter() {
        return filter;
    }

    public List<String> getDocumentIds() {
        return documentIds;
    }

    public static abstract class Builder<T extends Builder<T>> {
        protected Filter filter;
        protected List<String> documentIds;

        protected Builder() {
            this.documentIds = new ArrayList<>();
        }

        protected abstract T self();


        public T withFilter(Filter filter) {
            this.filter = filter;
            return self();
        }

        public T withDocumentIds(List<String> documentIds) {
            this.documentIds = documentIds;
            return self();
        }

        public T addDocumentId(String documentId) {
            this.documentIds.add(documentId);
            return self();
        }

        public T addAllDocumentId(List<String> documentIds) {
            this.documentIds.addAll(documentIds);
            return self();
        }

        public T addAllDocumentId(String... documentIds) {
            if(documentIds.length<1){
                return self();
            }
            for (String documentId : documentIds) {
                this.documentIds.add(documentId);
            }
            return self();
        }
    }
}
