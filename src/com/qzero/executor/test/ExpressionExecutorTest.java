package com.qzero.executor.test;

import com.qzero.executor.*;
import com.qzero.executor.constant.ConstantLoader;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.token.ExecutableAction;
import com.qzero.executor.variable.VariableLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ExpressionExecutorTest {

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

        ConstantLoader.addConstant("pi", Math.PI);
        ConstantLoader.addConstant("e", Math.E);


    }

    @Test
    public void testExecutor() throws Exception {
        String expression = "sin(pi)+2*cos(x)+ln(e)";
        VariableLoader.addOrEditVariable("x", new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE, Math.PI));
        //VariableLoader.addOrEditVariable("y", new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_STRING, "233"));
        List<ExpressionToken> analyzed = ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled = ExpressionCompiler.compile(analyzed);


        ExpressionExecutor.check(compiled);
        double result = ExpressionExecutor.executeCompiledExpression(compiled);
        System.out.println(String.format("%.4f", result));


    }

}
