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

package com.tencent.tcvectordb.model.param.dml;;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.tcvectordb.exception.ParamException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * VectorDB Filter
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Filter {

    private StringBuffer condBuffer;

    public Filter(String cond) {
        this.condBuffer = new StringBuffer(cond);
    }

    public Filter and(String cond) {
        this.condBuffer.append(String.format(" and ( %s )", cond));
        return this;
    }

    public Filter or(String cond) {
        this.condBuffer.append(String.format(" or ( %s )", cond));
        return this;
    }

    public Filter andNot(String cond) {
        this.condBuffer.append(String.format(" and not ( %s )", cond));
        return this;
    }

    public Filter orNot(String cond) {
        this.condBuffer.append(String.format(" or not ( %s )", cond));
        return this;
    }

    public static <T> String in(String key, List<T> values) {
        if (values == null || values.isEmpty()) {
            throw new ParamException("Filter in condition values is empty");
        }
        List<String> strValues = values.get(0) instanceof String ?
                values.stream().map(x -> "\"" + x + "\"").collect(Collectors.toList()) :
                values.stream().map(x -> x + "").collect(Collectors.toList());
        return String.format("%s in (%s)", key, String.join(",", strValues));
    }

    public static <T> String notIn(String key, List<T> values) {
        if (values == null || values.isEmpty()) {
            throw new ParamException("Filter in condition values is empty");
        }
        List<String> strValues = values.get(0) instanceof String ?
                values.stream().map(x -> "\"" + x + "\"").collect(Collectors.toList()) :
                values.stream().map(x -> x + "").collect(Collectors.toList());
        return String.format("%s not in (%s)", key, String.join(",", strValues));
    }

    public static <T> String include(String key, List<T> values) {
        if (values == null || values.isEmpty()) {
            throw new ParamException("Filter in condition values is empty");
        }
        List<String> strValues = values.get(0) instanceof String ?
                values.stream().map(x -> "\"" + x + "\"").collect(Collectors.toList()) :
                values.stream().map(x -> x + "").collect(Collectors.toList());
        return String.format("%s include (%s)", key, String.join(",", strValues));
    }

    public static <T> String exclude(String key, List<T> values) {
        if (values == null || values.isEmpty()) {
            throw new ParamException("Filter in condition values is empty");
        }
        List<String> strValues = values.get(0) instanceof String ?
                values.stream().map(x -> "\"" + x + "\"").collect(Collectors.toList()) :
                values.stream().map(x -> x + "").collect(Collectors.toList());
        return String.format("%s exclude (%s)", key, String.join(",", strValues));
    }

    public static <T> String includeAll(String key, List<T> values) {
        if (values == null || values.isEmpty()) {
            throw new ParamException("Filter in condition values is empty");
        }
        List<String> strValues = values.get(0) instanceof String ?
                values.stream().map(x -> "\"" + x + "\"").collect(Collectors.toList()) :
                values.stream().map(x -> x + "").collect(Collectors.toList());
        return String.format("%s include all (%s)", key, String.join(",", strValues));
    }

    public String getCond() {
        return condBuffer.toString();
    }
}
