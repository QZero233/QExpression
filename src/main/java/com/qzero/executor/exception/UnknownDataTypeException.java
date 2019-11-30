package com.qzero.executor.exception;

public class UnknownDataTypeException extends IllegalArgumentException {

    private String typeSimpleName;

    public UnknownDataTypeException(String typeSimpleName) {
        this.typeSimpleName = typeSimpleName;
    }

    @Override
    public String getMessage() {
        return "Unknown data type "+typeSimpleName;
    }
}
