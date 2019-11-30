package com.qzero.executor;

import com.qzero.executor.exception.*;
import com.qzero.executor.function.ExecutableAction;
import com.qzero.executor.function.ExecutableActionUtils;
import com.qzero.executor.function.IExecutableAction;
import com.qzero.executor.token.OperatorToken;
import com.qzero.executor.token.*;
import com.qzero.executor.variable.VariableEnv;

import java.util.ArrayList;
import java.util.Date;
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
    public static BaseDataMate executeCompiledExpression(List<ExpressionToken> rpnToken, VariableEnv env){
        if(env==null)
            env=new VariableEnv(null);

        if(rpnToken==null || rpnToken.isEmpty())
            throw new IllegalArgumentException("RPN Expression can not be null or empty");

        Stack<BaseDataMate> constantStack=new Stack<>();

        for(ExpressionToken token:rpnToken){
            if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT){
                //Constant,push into stack
                constantStack.push(((ConstantToken) token.getTokenObject()).getDataMate());
                continue;
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_VARIABLE){
                //Get the certain value of the variable and push into stack
                VariableToken variableToken= (VariableToken) token.getTokenObject();
                BaseDataMate variableValue= env.getVariableValue(variableToken.getTokenString());

                if(variableValue==null)
                    throw new VariableFoundNotException(variableToken.getTokenString());

                constantStack.push(variableValue);
                continue;
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR) {
                //Got an operational token,just do it
                OperatorToken operatorToken= (OperatorToken) token.getTokenObject();

                ExecutableAction executableAction=operatorToken.getAction();
                IExecutableAction action=getProperAction(executableAction,constantStack);

                if(action==null){
                    throw new OperationalTokenParameterException(token,constantStack,"executing expression");
                }

                int count=action.getParameterCount();

                BaseDataMate[] parameters=new BaseDataMate[count];
                for(int i=0;i<count;i++){
                    BaseDataMate dataMate=constantStack.pop();
                    parameters[count-1-i]=dataMate;
                }


                BaseDataMate result=action.execute(parameters);
                if(result==null)
                    throw new OperationalTokenExecuteFailedException(token,action.getParametersType(),parameters);

                constantStack.push(result);
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_FUNCTION){
                //Got an operational token,just do it
                IExecutableAction action;
                FunctionToken functionToken= (FunctionToken) token.getTokenObject();

                ExecutableAction executableAction=functionToken.getAction();
                action=getProperAction(executableAction,constantStack);

                if(action==null){
                    throw new OperationalTokenParameterException(token,constantStack,"executing expression");
                }

                int parameterCount=action.getParameterCount();

                BaseDataMate[] parameters=new BaseDataMate[parameterCount];
                for(int i=0;i<parameterCount;i++){
                    BaseDataMate dataMate=constantStack.pop();
                    parameters[parameterCount-1-i]=dataMate;
                }

                BaseDataMate result=action.execute(parameters);
                if(result==null)
                    throw new OperationalTokenExecuteFailedException(token,action.getParametersType(),parameters);

                constantStack.push(result);
            }
        }

        if(constantStack.size()!=1)
            throw new IllegalStateException("Execute failed,the size of constant stack is not 1");

        return constantStack.pop();
    }



    /**
     * Select a proper action in action pool
     * Choose the one which use less parameter when more than one action match parameter check
     * @param executableAction
     * @param constantStack
     * @return proper action,if none return null
     */
    private static IExecutableAction getProperAction(ExecutableAction executableAction,Stack<BaseDataMate> constantStack){
        if(constantStack==null || constantStack.isEmpty() || executableAction==null)
            return null;

        constantStack= (Stack<BaseDataMate>) constantStack.clone();

        List<Class> tmp=new ArrayList<>();
        IExecutableAction result=null;
        while (!constantStack.isEmpty()){
            BaseDataMate dataMate=constantStack.pop();
            Class type=ExecutableActionUtils.getTypeByDataMate(dataMate);
            tmp.add(type);

            try {
                String identifier= ExecutableActionUtils.getParameterListIdentifier(tmp);
                if(identifier!=null){
                    IExecutableAction action=executableAction.getAction(identifier);
                    if(action!=null){
                        result=action;
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return result;
    }
}
