package com.qzero.executor.token;

import com.qzero.executor.BaseDataMate;

/**
 * Class of variable token
 * @author QZero
 * @version 1.0
 */
public class VariableToken extends TokenObject {


    public VariableToken(String tokenString) {
        super(tokenString);
    }

    @Override
    public String toString() {
        return "VariableToken{" +
                "tokenString='" + tokenString + '\'' +
                '}';
    }
}
