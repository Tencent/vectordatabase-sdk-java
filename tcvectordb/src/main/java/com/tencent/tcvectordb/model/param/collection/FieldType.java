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

package com.tencent.tcvectordb.model.param.collection;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * FieldType
 */
public enum FieldType {
    /**
     * uint64
     */
    Uint64("uint64"),
    /**
     * string
     */
    String("string"),
    /**
     * vector
     */
    Vector("vector"),

    /**
     * float16_vector
     */
    Float16Vector("float16_vector"),

    /**
     * vector
     */
    BFloat16Vector("bfloat16_vector"),


    /**
     * binary vector
     */
    BinaryVector("binary_vector"),

    /**
     * array 类型
     */
    Array("array"),
    /**
     * sparse vector
     */
    SparseVector("sparseVector"),

    /**
     * Json 类型
     */
    Json("json");

    private final String value;

    private FieldType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static FieldType fromValue(String value) {
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType.getValue().equals(value)) {
                return fieldType;
            }
        }
        return null; // 或者抛出一个异常，如果value不合法
    }
}
