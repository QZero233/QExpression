package com.qzero.executor;

import com.qzero.executor.exception.*;
import com.qzero.executor.token.OperatorToken;
import com.qzero.executor.token.*;
import com.qzero.executor.variable.VariableEnv;

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
                ExecutableAction action=operatorToken.getAction();
                int count=action.getParameterCount();

                Class[] parameterTypes=action.getParametersType();
                BaseDataMate[] parameters=new BaseDataMate[count];
                for(int i=0;i<count;i++){
                    if(constantStack.isEmpty())
                        throw new WrongOperationalTokenParameterException(token,action);


                    BaseDataMate dataMate=constantStack.pop();
                    if(!checkType(parameterTypes[count-1-i],dataMate))
                        throw new WrongOperationalTokenParameterException(token,parameterTypes[count-1-i],dataMate.getDataValue().getClass(),i);

                    parameters[count-1-i]=dataMate;
                }


                BaseDataMate result=action.execute(parameters);
                if(result==null)
                    throw new IllegalArgumentException("Exception in executing operator "+token.getTokenObject().getTokenString());

                constantStack.push(result);
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_FUNCTION){
                //Got an operational token,just do it
                ExecutableAction action;
                FunctionToken functionToken= (FunctionToken) token.getTokenObject();
                action=functionToken.getAction();

                int parameterCount=action.getParameterCount();
                Class[] parameterTypes=action.getParametersType();
                BaseDataMate[] parameters=new BaseDataMate[parameterCount];
                for(int i=0;i<parameterCount;i++){

                    if(constantStack.isEmpty())
                        throw new WrongOperationalTokenParameterException(token,action);


                    BaseDataMate dataMate=constantStack.pop();
                    if(!checkType(parameterTypes[parameterCount-1-i],dataMate))
                        throw new WrongOperationalTokenParameterException(token,parameterTypes[parameterCount-1-i],dataMate.getDataValue().getClass(),i);

                    parameters[parameterCount-1-i]=dataMate;
                }

                BaseDataMate result=action.execute(parameters);
                if(result==null)
                    throw new IllegalArgumentException("Exception in executing function "+token.getTokenObject().getTokenString());

                constantStack.push(result);
            }
        }

        if(constantStack.size()!=1)
            throw new IllegalStateException("Execute failed,the size of constant stack is not 1");

        return constantStack.pop();
    }

    private static boolean checkType(Class cls,BaseDataMate dataMate){
        if(cls==null || dataMate==null)
            return false;
        if(dataMate.getDataType()== BaseDataMate.DataType.DATA_TYPE_UNKNOWN)
            return true;
        if(dataMate.getDataType()== BaseDataMate.DataType.DATA_TYPE_STRING && cls.equals(String.class))
            return true;
        else if(dataMate.getDataType()== BaseDataMate.DataType.DATA_TYPE_DOUBLE && cls.equals(Double.class))
            return true;
        else if(dataMate.getDataType()== BaseDataMate.DataType.DATA_TYPE_BOOLEAN && cls.equals(Boolean.class))
            return true;
        return false;
    }

    private static BaseDataMate newDataMateByClass(Class cls){
        if(cls==null)
            return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_UNKNOWN,null);
        else if(cls.equals(String.class))
            return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_STRING,"");
        else if(cls.equals(Double.class))
            return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,0D);
        else if(cls.equals(Boolean.class))
            return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,new Boolean(true));
        else
            throw new IllegalArgumentException("Unknown data type");
    }

    /**
     * Check whether a compiled expression is ready to execute or not
     * If there is something wrong with the expression(such as variable missing),it'll throw exception
     * @param compiledTokenList The compiled token list
     * @return If the compiled expression is ready to execute return,otherwise throw
     * @throws ConstantFoundNotException If a constant doesn't have a certain value,it'll be thrown
     * @throws VariableFoundNotException If a variable doesn't have a certain value,it'll be thrown
     * @throws FunctionFoundNotException If a function doesn't have a certain action,it'll be thrown
     * @throws WrongOperationalTokenParameterException If a function or an operator can not get enough and suitable parameters,it'll be thrown
     * @throws WrongExpressionException If the size of constant stack is not 1 when executing over,which means the expression is wrong,it'll be thrown
     */
    public static void check(List<ExpressionToken> compiledTokenList) throws
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
                constantStack.push(new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_UNKNOWN,null));
                continue;
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR){
                //Got an operational token,just do it
                OperatorToken operatorToken= (OperatorToken) token.getTokenObject();
                ExecutableAction action=operatorToken.getAction();
                int count=action.getParameterCount();

                BaseDataMate operatorParameters[]=new BaseDataMate[count];

                for(int i=0;i<count;i++){
                    if(constantStack.isEmpty())
                        throw new WrongOperationalTokenParameterException(token,action);

                    operatorParameters[i]=constantStack.pop();
                }

                //Check if the type is suitable
                Class[] operatorParameterTypes=action.getParametersType();
                for(int i=0;i<count;i++){
                    if(!checkType(operatorParameterTypes[i],operatorParameters[i]))
                        throw new WrongOperationalTokenParameterException(token,operatorParameterTypes[i],operatorParameters[i].getDataValue().getClass(),i);
                }

                Class returnValue=action.getReturnValueType();
                if(returnValue==null){
                    throw new IllegalArgumentException("The return value of "+token.getTokenObject().getTokenString()+" can not be null");
                }

                constantStack.push(newDataMateByClass(returnValue));

            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_FUNCTION){
                //Got an operational token,just do it
                ExecutableAction action;
                FunctionToken functionToken= (FunctionToken) token.getTokenObject();
                action=functionToken.getAction();


                if(action==null)
                    throw new FunctionFoundNotException(token.getTokenObject().getTokenString());

                int parameterCount=action.getParameterCount();
                if(constantStack.size()<parameterCount)
                    throw new WrongOperationalTokenParameterException(token,action);

                Class[] types=action.getParametersType();
                if(types==null || types.length<parameterCount)
                    throw new WrongOperationalTokenParameterException(token,action);

                for(int i=0;i<parameterCount;i++){
                    BaseDataMate dataMate=constantStack.pop();

                    Class parameterType=types[parameterCount-1-i];
                    if(!checkType(parameterType,dataMate))
                        throw new WrongOperationalTokenParameterException(token,parameterType,dataMate.getDataValue().getClass(),i);
                }

                Class returnValue=action.getReturnValueType();
                if(returnValue==null){
                    throw new IllegalArgumentException("The return value of "+token.getTokenObject().getTokenString()+" can not be null");
                }

                constantStack.push(newDataMateByClass(returnValue));
            }
        }

        if(constantStack.size()!=1)
            throw new WrongExpressionException();

        constantStack.clear();
    }

}
