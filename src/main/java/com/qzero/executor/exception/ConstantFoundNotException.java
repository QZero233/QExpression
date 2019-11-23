package com.qzero.executor.exception;

public class ConstantFoundNotException extends IllegalArgumentException {

    private String constantName;

    public ConstantFoundNotException(String constantName) {
        super("Constant "+constantName+" has not been found in global constant loader when executing");
        this.constantName = constantName;
    }

    public String getConstantName() {
        return constantName;
    }

    public void setConstantName(String constantName) {
        this.constantName = constantName;
    }
}
