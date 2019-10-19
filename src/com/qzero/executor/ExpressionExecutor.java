package com.qzero.executor;

import com.qzero.executor.exception.*;
import com.qzero.executor.token.*;
import com.qzero.executor.variable.VariableLoader;

import java.util.ArrayList;
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

        Stack<BaseDataMate> constantStack=new Stack<>();

        for(ExpressionToken token:rpnToken){
            if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT){
                //Constant,push into stack
                constantStack.push(((ConstantToken) token.getTokenObject()).getDataMate());
                continue;
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_VARIABLE){
                //Get the certain value of the variable and push into stack
                VariableToken variableToken= (VariableToken) token.getTokenObject();
                BaseDataMate variableValue= VariableLoader.getVariableValue(variableToken.getTokenString());
                constantStack.push(variableValue);
                continue;
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR || token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_FUNCTION){
                //Got an operational token,just do it
                ExecutableAction action;
                if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR){
                    OperatorToken operatorToken= (OperatorToken) token.getTokenObject();
                    action=operatorToken.getAction();
                }else{
                    FunctionToken functionToken= (FunctionToken) token.getTokenObject();
                    action=functionToken.getAction();
                }

                int parameterCount=action.getParameterCount();
                Object[] parameters=new Object[parameterCount];
                for(int i=0;i<parameterCount;i++){

                    if(constantStack.isEmpty())
                        throw new WrongOperationalTokenParameterException(token.getTokenObject().getTokenString(),action,"");


                    BaseDataMate dataMate=constantStack.pop();
                    parameters[parameterCount-1-i]=dataMate.getDataValue();
                }

                Double result=action.execute(parameters);
                if(result==null)
                    throw new IllegalArgumentException("Exception in executing function "+token.getTokenObject().getTokenString());

                constantStack.push(new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,result));
            }
        }

        if(constantStack.size()!=1)
            throw new IllegalStateException("Execute failed,the size of constant stack is not 1");

        return (Double) constantStack.pop().getDataValue();
    }

    /**
     * Check whether a compiled expression is ready to execute or not
     * If there is something wrong with the expression(such as variable missing),it'll throw exception
     * @param compiledTokenList The compiled token list
     * @param doVariableCheck if true,it'll turn to global variable loader to check whether the variable has a certain value or not
     * @return If the compiled expression is ready to execute return,otherwise throw
     * @throws ConstantFoundNotException If a constant doesn't have a certain value,it'll be thrown
     * @throws VariableFoundNotException If a variable doesn't have a certain value,it'll be thrown
     * @throws FunctionFoundNotException If a function doesn't have a certain action,it'll be thrown
     * @throws WrongOperationalTokenParameterException If a function or an operator can not get enough and suitable parameters,it'll be thrown
     * @throws WrongExpressionException If the size of constant stack is not 1 when executing over,which means the expression is wrong,it'll be thrown
     */
    public static void check(List<ExpressionToken> compiledTokenList,boolean doVariableCheck) throws
            ConstantFoundNotException,VariableFoundNotException, FunctionFoundNotException,
            WrongOperationalTokenParameterException,WrongExpressionException {

        if(compiledTokenList==null || compiledTokenList.isEmpty())
            throw new IllegalArgumentException("Compiled token list can not be null");

        Stack<BaseDataMate> constantStack=new Stack<>();
        for(ExpressionToken token:compiledTokenList){
            if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT){
                //Constant,push into stack
                ConstantToken constantToken= (ConstantToken) token.getTokenObject();
                BaseDataMate dataMate=constantToken.getDataMate();
                if(dataMate==null || dataMate.getDataValue()==null)
                    throw new ConstantFoundNotException(constantToken.getTokenString());

                constantStack.push(dataMate);
                continue;
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_VARIABLE){
                //Get the certain value of the variable and push into stack

                if(!doVariableCheck){
                    constantStack.push(new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,0D));
                    continue;
                }

                VariableToken variableToken= (VariableToken) token.getTokenObject();
                BaseDataMate variableValue= VariableLoader.getVariableValue(variableToken.getTokenString());
                if(variableValue==null)
                    throw new VariableFoundNotException(variableToken.getTokenString());

                constantStack.push(variableValue);
                continue;
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR || token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_FUNCTION){
                //Got an operational token,just do it
                ExecutableAction action;
                if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR){
                    OperatorToken operatorToken= (OperatorToken) token.getTokenObject();
                    action=operatorToken.getAction();
                }else{
                    FunctionToken functionToken= (FunctionToken) token.getTokenObject();
                    action=functionToken.getAction();
                }

                if(action==null)
                    throw new FunctionFoundNotException(token.getTokenObject().getTokenString());

                int parameterCount=action.getParameterCount();
                if(constantStack.size()<parameterCount)
                    throw new WrongOperationalTokenParameterException(token.getTokenObject().getTokenString(),action,"");

                Class[] types=action.getParametersType();
                if(types==null || types.length<parameterCount)
                    throw new WrongOperationalTokenParameterException(token.getTokenObject().getTokenString(),action,
                            "the given action doesn't provide enough info of parameters");

                for(int i=0;i<parameterCount;i++){
                    BaseDataMate dataMate=constantStack.pop();
                    Object value=dataMate.getDataValue();

                    Class parameterType=types[parameterCount-1-i];
                    if(!value.getClass().equals(parameterType))
                        throw new WrongOperationalTokenParameterException(token.getTokenObject().getTokenString(),action,
                                "the type should be "+parameterType.getSimpleName()+" not "+value.getClass().getSimpleName());
                }


                constantStack.push(new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,0D));
            }
        }

        if(constantStack.size()!=1)
            throw new WrongExpressionException();

        constantStack.clear();
    }

}
