/*
 * Copyright (C) 2023 Tencent Cloud.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the vectordb-sdk-java), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.tcvdb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * IndexType
 */
public enum IndexType {
    /**
     * vector index type: flat
     */
    FLAT("FLAT"),
    /**
     * vector index type: hnsw
     */
    HNSW("HNSW"),
    /**
     *
     */
    IVF_FLAT("IVF_FLAT"),
    /**
     *
     */
    IVF_PQ("IVF_PQ"),
    /**
     *
     */
    IVF_SQ4("IVF_SQ4"),
    /**
     *
     */
    IVF_SQ8("IVF_SQ8"),
    /**
     *
     */
    IVF_SQ16("IVF_SQ16"),
    /**
     * scalar index type: primaryKey
     */
    PRIMARY_KEY("primaryKey"),
    /**
     * scalar index type: filter
     */
    FILTER("filter"),

    SPARSE_INVERTED("inverted");

    private final String value;

    private IndexType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
