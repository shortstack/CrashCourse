package com.whitneychampion.crashcourse.api;

import com.whitneychampion.crashcourse.model.ApiBase;

/**
 * Created by Whitney Champion on 8/23/14.
 */

public class ApiError extends ApiBase {

    private String errorMessage;
    private String errorCode;
    private Boolean success;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}