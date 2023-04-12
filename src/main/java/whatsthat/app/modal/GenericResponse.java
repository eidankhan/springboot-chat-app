package whatsthat.app.modal;

public class GenericResponse {
    private Integer code;
    private String description;
    private Object data;

    public GenericResponse(){}
    public GenericResponse(Integer code, String description){
        this.code = code;
        this.description = description;
    }
    public GenericResponse(Integer code, String description, Object data){
        this.code = code;
        this.description = description;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
