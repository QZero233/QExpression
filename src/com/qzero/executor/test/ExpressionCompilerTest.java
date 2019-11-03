package com.qzero.executor.test;

import com.qzero.executor.*;
import com.qzero.executor.constant.ConstantLoader;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.token.ExecutableAction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ExpressionCompilerTest {

    @Before
    public void loadTestData(){
        Init.init();
    }

    @Test
    public void testCompiler(){
        String expression="sin(pi)+2*cos(x)";
        List<ExpressionToken> analyzed= ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled= ExpressionCompiler.compile(analyzed);
        for(ExpressionToken token:compiled){
            System.out.println(token);
        }
    }



}
