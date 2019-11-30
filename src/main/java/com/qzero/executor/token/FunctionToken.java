package com.qzero.executor.token;

import com.qzero.executor.function.ExecutableAction;

/**
 * Class of function token
 * @author QZero
 * @version 1.0
 */
public class FunctionToken extends OperationalToken {
    public FunctionToken(String tokenString, ExecutableAction action) {
        super(tokenString,action);
    }
}
