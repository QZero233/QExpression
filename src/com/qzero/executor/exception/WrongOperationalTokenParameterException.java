package com.qzero.executor.exception;

import com.qzero.executor.token.ExecutableAction;

public class WrongOperationalTokenParameterException extends IllegalArgumentException {

    private String operationalName;
    private ExecutableAction action;

    public WrongOperationalTokenParameterException(String operationalName, ExecutableAction action,String more) {
        super("Wrong operational token parameter when compiling operational token "+operationalName+",the count should be "+action.getParameterCount()+","+more);
        this.operationalName = operationalName;
        this.action = action;
    }

    public String getOperationalName() {
        return operationalName;
    }

    public void setOperationalName(String operationalName) {
        this.operationalName = operationalName;
    }

    public ExecutableAction getAction() {
        return action;
    }

    public void setAction(ExecutableAction action) {
        this.action = action;
    }
}
