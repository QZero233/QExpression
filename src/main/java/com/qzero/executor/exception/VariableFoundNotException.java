package com.qzero.executor.exception;

public class VariableFoundNotException extends IllegalArgumentException {
    private String variableName;

    public VariableFoundNotException(String variableName) {
        super("Variable "+variableName+" has not been found in global variable loader when executing");
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
}
