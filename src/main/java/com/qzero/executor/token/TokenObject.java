package com.qzero.executor.token;

/**
 * Base class of token object
 * @author QZero
 * @version 1.0
 */
public class TokenObject {

    //Describe token in string(like operator name or function name)
    protected String tokenString;

    public TokenObject(String tokenString) {
        this.tokenString = tokenString;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    @Override
    public String toString() {
        return "TokenObject{" +
                "tokenString='" + tokenString + '\'' +
                '}';
    }
}
