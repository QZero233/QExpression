package com.qzero.executor.test;

import com.qzero.executor.*;
import com.qzero.executor.constant.ConstantLoader;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.token.ExecutableAction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MinusFixTest {

    @Before
    public void loadTestData() {
        Init.init();
    }

    @Test
    public void testFixMinus(){

        //Circle: x^2+y^2=1 -> y=-sqrt(1-pow(x,2))

        String expression="-sqrt(1-pow(x/2,2))";

        List<ExpressionToken> analyzed=ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled=ExpressionCompiler.compile(analyzed);

        ExpressionExecutor.check(compiled);

        String latex=ExpressionLatexTranslator.translateToLatex(compiled);
        System.out.println(latex);

        //double result=ExpressionExecutor.executeCompiledExpression(compiled);
        //System.out.println(result+"");
    }

}
