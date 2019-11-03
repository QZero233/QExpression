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
        ExecutableAction sinAction = new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 1;
            }

            @Override
            public double execute(Object[] parameters) {
                return Math.sin((Double) parameters[0]);
            }

            @Override
            public Class[] getParametersType() {
                return new Class[]{Double.class};
            }
        };

        ExecutableAction cosAction = new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 1;
            }

            @Override
            public double execute(Object[] parameters) {
                return Math.cos((Double) parameters[0]);
            }

            @Override
            public Class[] getParametersType() {
                return new Class[]{Double.class};
            }
        };


        ExecutableAction sqrtAction = new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 1;
            }

            @Override
            public double execute(Object[] parameters) {
                return Math.sqrt((Double) parameters[0]);
            }

            @Override
            public Class[] getParametersType() {
                return new Class[]{Double.class};
            }
        };

        ExecutableAction powAction = new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 2;
            }

            @Override
            public double execute(Object[] parameters) {
                return Math.pow((Double) parameters[0],(Double) parameters[1]);
            }

            @Override
            public Class[] getParametersType() {
                return new Class[]{Double.class,Double.class};
            }
        };

        ExecutableAction lnAction = new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 1;
            }

            @Override
            public double execute(Object[] parameters) {
                return Math.log((Double) parameters[0]);
            }

            @Override
            public Class[] getParametersType() {
                return new Class[]{Double.class};
            }
        };


        FunctionLoader.addFunction("sin", sinAction);
        FunctionLoader.addFunction("cos", cosAction);
        FunctionLoader.addFunction("ln", lnAction);
        FunctionLoader.addFunction("pow", powAction);
        FunctionLoader.addFunction("sqrt", sqrtAction);

        ConstantLoader.addConstant("pi", Math.PI);
        ConstantLoader.addConstant("e", Math.E);
        ConstantLoader.addConstant("G", 6.67E-11);
    }

    @Test
    public void testFixE(){
        String expression="-sqrt(1-pow(1/2,2))";
        List<ExpressionToken> analyzed=ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled=ExpressionCompiler.compile(analyzed);

        ExpressionExecutor.check(compiled);

        System.out.println(ExpressionLatexTranslator.translateToLatex(compiled));
        System.out.println(ExpressionExecutor.executeCompiledExpression(compiled,new VariableEnv(null)));

    }

}
