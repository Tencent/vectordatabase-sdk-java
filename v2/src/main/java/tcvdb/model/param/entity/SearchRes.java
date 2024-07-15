package tcvdb.model.param.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import tcvdb.model.Document;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchRes extends BaseRes {
    private List<List<Document>> documents;

    public List<List<Document>> getDocuments() {
        return documents;
    }

    public SearchRes(int code, String msg, String warning, List<List<Document>> documents) {
        super(code, msg, warning);
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "SearchByEmRes{" +
                "documents=" + documents +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", warning='" + warning + '\'' +
                '}';
    }
}
