package com.qzero.executor.exception;

import com.qzero.executor.token.VariableToken;

public class VariableFoundNotException extends IllegalArgumentException {
    private VariableToken variableToken;

    public VariableFoundNotException(String s, VariableToken variableToken) {
        super(s);
        this.variableToken = variableToken;
    }

    public VariableToken getVariableToken() {
        return variableToken;
    }

    public void setVariableToken(VariableToken variableToken) {
        this.variableToken = variableToken;
    }
}
