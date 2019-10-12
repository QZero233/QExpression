package com.qzero.executor.token;

import com.qzero.executor.BaseDataMate;

/**
 * Class of variable token
 * @author QZero
 * @version 1.0
 */
public class VariableToken extends TokenObject {

    private BaseDataMate variableValue;

    public VariableToken(String tokenString, BaseDataMate variableValue) {
        super(tokenString);
        this.variableValue = variableValue;
    }

    public BaseDataMate getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(BaseDataMate variableValue) {
        this.variableValue = variableValue;
    }

    @Override
    public String toString() {
        return "VariableToken{" +
                "variableValue=" + variableValue +
                ", tokenString='" + tokenString + '\'' +
                '}';
    }
}
