package com.qzero.executor.token;

import com.qzero.executor.function.ExecutableAction;

public class OperationalToken extends TokenObject {

    protected ExecutableAction action;

    public OperationalToken(String tokenString, ExecutableAction action) {
        super(tokenString);
        this.action = action;
    }

    public ExecutableAction getAction() {
        return action;
    }

    public void setAction(ExecutableAction action) {
        this.action = action;
    }
}
