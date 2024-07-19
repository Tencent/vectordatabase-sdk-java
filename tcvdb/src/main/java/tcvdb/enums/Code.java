package tcvdb.enums;

public enum Code {
    Success(0, "success");

    private final int code;
    private final String msg;

    Code(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static boolean isSuccess(int code) {
        return Success.code == code;
    }

    public static boolean isFailed(int code) {
        return !isSuccess(code);
    }
}
