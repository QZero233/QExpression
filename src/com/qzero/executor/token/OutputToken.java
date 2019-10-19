package com.qzero.executor.token;

/**
 * Token Object only for json output
 * @author QZero
 * @version 1.0
 */
public class OutputToken {

    private String tokenString;
    private int tokenType;

    public static final int TYPE_CONSTANT=0;
    public static final int TYPE_VARIABLE=1;
    public static final int TYPE_FUNCTION=2;
    public static final int TYPE_OPERATOR=3;

    public OutputToken() {
    }

    public OutputToken(String tokenString, int tokenType) {
        this.tokenString = tokenString;
        this.tokenType = tokenType;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public int getTokenType() {
        return tokenType;
    }

    public void setTokenType(int tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return "OutputToken{" +
                "tokenString='" + tokenString + '\'' +
                ", tokenType=" + tokenType +
                '}';
    }
}
