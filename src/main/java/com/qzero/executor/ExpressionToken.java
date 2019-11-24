package com.qzero.executor;

import com.qzero.executor.token.TokenObject;

/**
 * Class of expression token
 * @author QZero
 * @version 1.0
 */
public class ExpressionToken {

    public enum TokenType{
        TOKEN_TYPE_CONSTANT,
        TOKEN_TYPE_VARIABLE,
        TOKEN_TYPE_FUNCTION,
        TOKEN_TYPE_OPERATOR,
        TOKEN_TYPE_SPLIT,};

    private TokenType tokenType;
    //A certain object of this token which contains name,necessary parameters etc.
    private TokenObject tokenObject;

    private int startAt,endAt;

    public ExpressionToken() {
    }

    public ExpressionToken(TokenType tokenType, TokenObject tokenObject, int endAt) {
        this.tokenType = tokenType;
        this.tokenObject = tokenObject;
        endAt++;
        this.endAt = endAt;
        startAt=endAt-tokenObject.getTokenString().length();
    }

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public int getEndAt() {
        return endAt;
    }

    public void setEndAt(int endAt) {
        this.endAt = endAt;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public TokenObject getTokenObject() {
        return tokenObject;
    }

    public void setTokenObject(TokenObject tokenObject) {
        this.tokenObject = tokenObject;
    }

    @Override
    public String toString() {
        return "ExpressionToken{" +
                "tokenType=" + tokenType +
                ", tokenObject=" + tokenObject +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                '}';
    }
}
