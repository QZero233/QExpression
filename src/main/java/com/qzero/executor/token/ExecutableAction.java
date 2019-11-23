package com.qzero.executor.token;

import com.qzero.executor.BaseDataMate;

/**
 * Interface for executable action(such as operator or function)
 * @author QZero
 * @version 1.0
 */
public interface ExecutableAction {

    int getParameterCount();
    BaseDataMate execute(BaseDataMate[] parameters);
    Class[] getParametersType();

}
