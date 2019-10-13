package com.qzero.executor.exception;

public class FunctionFoundNotException extends IllegalArgumentException {

    private String functionName;

    public FunctionFoundNotException(String functionName) {
        super("Function "+functionName+" has not been found in global function loader when compiling");
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
}
