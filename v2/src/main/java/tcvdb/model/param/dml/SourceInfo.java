package tcvdb.model.param.dml;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SourceInfo {
    private String _vdc_source_name;
    private String _vdc_source_type;
    private String _vdc_source_path;

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
}
