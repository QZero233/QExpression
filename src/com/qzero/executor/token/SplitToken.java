package com.qzero.executor.token;

/**
 * Class of split token
 * @author QZero
 * @version 1.0
 */
public class SplitToken extends TokenObject {

    public enum SplitType {
        SPLIT_TYPE_BRACKETS_LEFT,
        SPLIT_TYPE_BRACKETS_RIGHT,
        SPLIT_TYPE_COMMA
    };

    private SplitType type;

    public SplitToken(String tokenString, SplitType type) {
        super(tokenString);
        this.type = type;
    }

    public SplitType getType() {
        return type;
    }

    public void setType(SplitType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SplitToken{" +
                "type=" + type +
                ", tokenString='" + tokenString + '\'' +
                '}';
    }
}
