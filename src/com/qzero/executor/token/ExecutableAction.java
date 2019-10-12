package com.qzero.executor.token;

/**
 * Interface for executable action(such as operator or function)
 * @author QZero
 * @version 1.0
 */
public interface ExecutableAction {

    int getParameterCount();
    double execute(Object[] parameters);

}
