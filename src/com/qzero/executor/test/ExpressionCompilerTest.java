package com.qzero.executor.test;

import com.qzero.executor.*;
import com.qzero.executor.constant.ConstantLoader;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.token.ExecutableAction;
import com.qzero.executor.variable.VariableLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ExpressionCompilerTest {

    @Before
    public void loadTestData(){
        ExecutableAction emptyAction=new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 1;
            }

            @Override
            public double execute(Object[] parameters) {
                return 0;
            }

            @Override
            public Class[] getParametersType() {
                return new Class[]{Object.class};
            }
        };



        FunctionLoader.addFunction("sin",emptyAction);
        FunctionLoader.addFunction("cos",emptyAction);

        ConstantLoader.addConstant("pi",Math.PI);
        ConstantLoader.addConstant("e",Math.E);


    }

    @Test
    public void testCompiler(){
        String expression="sin(pi)+2*cos(x)";
        VariableLoader.addOrEditVariable("x",new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,Math.PI));
        List<ExpressionToken> analyzed= ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled= ExpressionCompiler.compile(analyzed);
        for(ExpressionToken token:compiled){
            System.out.println(token);
        }
    }

}
