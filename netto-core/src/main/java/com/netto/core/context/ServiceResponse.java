package com.netto.core.context;

public class ServiceResponse<T> {

    private String errorMessage;

    private Boolean success = false;

    private T retObject;

    public T getRetObject() {
        return retObject;
    }

    public void setRetObject(T retObject) {
        this.retObject = retObject;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
