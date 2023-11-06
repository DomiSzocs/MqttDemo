package edu.MttqDemo.Model;

import java.io.Serializable;

public class ServerResponse implements Serializable {
    private String advice;

    public ServerResponse() {

    }

    public ServerResponse(String advice) {
        this.advice = advice;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
