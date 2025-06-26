package com.tencent.tcvectordb.model.param.entity;


public class FileDetail {
    private String id;
    private String _indexed_status;
    private Long _text_length;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_indexed_status() {
        return _indexed_status;
    }

    public void set_indexed_status(String _indexed_status) {
        this._indexed_status = _indexed_status;
    }

    public Long get_text_length() {
        return _text_length;
    }

    public void set_text_length(Long _text_length) {
        this._text_length = _text_length;
    }
}
