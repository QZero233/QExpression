package com.qzero.executor.test;

import com.qzero.executor.*;
import com.qzero.executor.variable.VariableEnv;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooleanTest {

    @Before
    public void load(){
        Init.init();
    }

    @Test
    public void testBoolean(){
        String expression="!(sqrt(5)>2)||(2==3)||(x<=2)";

        List<ExpressionToken> analyzed= ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled= ExpressionCompiler.compile(analyzed);

        ExpressionExecutor.check(compiled);

        Map<String,BaseDataMate> env=new HashMap<>();
        env.put("x",new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,2D));
        BaseDataMate result=ExpressionExecutor.executeCompiledExpression(compiled,new VariableEnv(env));
        System.out.println(result);
    }

}
