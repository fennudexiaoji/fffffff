package com.common.lib.retrofit.model;


import java.util.List;

public class BaseResponse<T>{
    public static int SUCCESS_CODE=0;
    private boolean success;
    //private int code;
    private int error;
    //private String msg;
    private List<String> msg;
    private T data;
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getMsg() {
        return msg;
    }

    public void setMsg(List<String> msg) {
        this.msg = msg;
    }
/*public void setMsg(String msg) {
        this.msg = msg;
    }*/

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
/*public int getError() {
        return code;
    }

    public void setError(int error) {
        this.code = error;
    }*/

    /*public String getMsg() {
        return msg;
    }*/


}
