package com.qzero.executor.token;

/**
 * Class of function token
 * @author QZero
 * @version 1.0
 */
public class FunctionToken extends TokenObject {

    private ExecutableAction action;

    public FunctionToken(String tokenString, ExecutableAction action) {
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
