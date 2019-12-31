package com.example.exception;

public class BizException extends Exception {
    /**
     * @fields serialVersionUID
     */

    private static final long serialVersionUID = -2420754730135049197L;
    private Object data;

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

    public Object getData() {
        return this.data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}
