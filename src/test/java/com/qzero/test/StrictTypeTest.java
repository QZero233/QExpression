package com.qzero.test;

import com.qzero.executor.ExpressionExecutor;
import com.qzero.executor.ExpressionToken;
import com.qzero.executor.variable.VariableEnv;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class StrictTypeTest {

    @Before
    public void init(){
        Init.init();
    }

    @Test
    public void strictTypeTest(){
        String script="sqrt(x)";

        List<ExpressionToken> compiled=Init.getCompiled(script);
        System.out.println();
    }

}
