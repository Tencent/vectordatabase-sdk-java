package com.tencent.tcvdb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SourceInfo {
    private String _vdc_source_name;
    private String _vdc_source_type;
    private String _vdc_source_path;

    public SourceInfo() {
    }

    public SourceInfo(Builder builder) {
        this._vdc_source_name = builder._vdc_source_name;
        this._vdc_source_type = builder._vdc_source_type;
        this._vdc_source_path = builder._vdc_source_path;
    }

    public String get_vdc_source_name() {
        return _vdc_source_name;
    }

    public void set_vdc_source_name(String _vdc_source_name) {
        this._vdc_source_name = _vdc_source_name;
    }

    public String get_vdc_source_type() {
        return _vdc_source_type;
    }

    public void set_vdc_source_type(String _vdc_source_type) {
        this._vdc_source_type = _vdc_source_type;
    }

    public String get_vdc_source_path() {
        return _vdc_source_path;
    }

    public void set_vdc_source_path(String _vdc_source_path) {
        this._vdc_source_path = _vdc_source_path;
    }

    // 建造者模式
    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder {
        private String _vdc_source_name;
        private String _vdc_source_type;
        private String _vdc_source_path;
        public Builder() {

        }
        public Builder with_vdc_source_name(String _vdc_source_name) {
            this._vdc_source_name = _vdc_source_name;
            return this;
        }
        public Builder with_vdc_source_type(String _vdc_source_type) {
            this._vdc_source_type = _vdc_source_type;
            return this;
        }
        public Builder with_vdc_source_path(String _vdc_source_path) {
            this._vdc_source_path = _vdc_source_path;
            return this;
        }
        public SourceInfo build() {
            return new SourceInfo(this);
        }
    }
}
