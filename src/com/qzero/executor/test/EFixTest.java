package com.qzero.executor.test;

import com.qzero.executor.*;
import com.qzero.executor.constant.ConstantLoader;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.token.ExecutableAction;
import com.qzero.executor.variable.VariableEnv;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class EFixTest {

    @Before
    public void loadTestData() {
        Init.init();
    }

    @Test
    public void testFixE(){
        String expression="-b+sqrt(pow(b,2)-4*a*c)/(2*a)";
        List<ExpressionToken> analyzed=ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled=ExpressionCompiler.compile(analyzed);

        ExpressionExecutor.check(compiled);

        System.out.println(ExpressionLatexTranslator.translateToLatex(compiled));
        System.out.println(ExpressionExecutor.executeCompiledExpression(compiled,new VariableEnv(null)));

    }

}
