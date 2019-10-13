package com.qzero.executor.exception;

public class WrongExpressionException extends IllegalArgumentException {

    public WrongExpressionException() {
        super("Wrong expression,constant stack's size is not 1 when executing over");
    }
}
