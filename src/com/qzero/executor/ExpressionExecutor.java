package com.qzero.executor;

import com.qzero.executor.exception.VariableFoundNotException;
import com.qzero.executor.token.*;
import com.qzero.executor.variable.VariableLoader;

import java.util.List;
import java.util.Stack;

/**
 * An executor to execute compiled expression
 * @author QZero
 * @version 1.0
 */
public class ExpressionExecutor {

    /**
     * Execute a compiled expression
     * @param rpnToken expression compiled into RPN
     * @return The value of the expression
     * @throws
     */
    public static double executeCompiledExpression(List<ExpressionToken> rpnToken){
        if(rpnToken==null || rpnToken.isEmpty())
            throw new IllegalArgumentException("RPN Expression can not be null or empty");

        Stack<ConstantToken> constantStack=new Stack<>();

        for(ExpressionToken token:rpnToken){
            if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT){
                //Constant,push into stack
                constantStack.push((ConstantToken) token.getTokenObject());
                continue;
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_VARIABLE){
                //Get the certain value of the variable and push into stack
                VariableToken variableToken= (VariableToken) token.getTokenObject();
                BaseDataMate variableValue= VariableLoader.getVariableValue(variableToken.getTokenString());
                if(variableValue==null)
                    throw new VariableFoundNotException("Variable "+token.getTokenObject().getTokenString()+" has not beem found in global variable loader",
                            variableToken);

                ConstantToken constantToken=new ConstantToken(variableValue.getDataValue().toString(),
                        variableValue.getDataType(),variableValue.getDataValue());
                constantStack.push(constantToken);
                continue;
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR || token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_FUNCTION){
                //Got an operational token,just do it
                ExecutableAction action=null;
                if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR){
                    OperatorToken operatorToken= (OperatorToken) token.getTokenObject();
                    action=operatorToken.getAction();
                }else{
                    FunctionToken functionToken= (FunctionToken) token.getTokenObject();
                    action=functionToken.getAction();
                }

                if(action==null)
                    throw new IllegalArgumentException("The action of function "+token.getTokenObject().getTokenString()+" can not be null");

                int parameterCount=action.getParameterCount();
                Object[] parameters=new Object[parameterCount];
                for(int i=0;i<parameterCount;i++){

                    if(constantStack.isEmpty())
                        throw new IllegalArgumentException("Parameter count wrong,in executing function "+token.getTokenObject().getTokenString());

                    ConstantToken constantToken=constantStack.pop();
                    parameters[parameterCount-1-i]=constantToken.getDataMate().getDataValue();
                }

                Double result=action.execute(parameters);
                if(result==null)
                    throw new IllegalArgumentException("Exception in executing function "+token.getTokenObject().getTokenString());

                ConstantToken resultToken=new ConstantToken(result.toString(), BaseDataMate.DataType.DATA_TYPE_DOUBLE,result);
                constantStack.push(resultToken);
            }
        }

        if(constantStack.size()!=1)
            throw new IllegalStateException("Execute failed,the size of constant stack is not 1");

        return (Double) constantStack.pop().getDataMate().getDataValue();
    }

}
