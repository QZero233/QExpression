package com.qzero.executor.exception;

import com.qzero.executor.BaseDataMate;
import com.qzero.executor.ExpressionToken;
import com.qzero.executor.function.ExecutableActionUtils;
import com.qzero.executor.function.IExecutableAction;
import com.qzero.executor.token.OperationalToken;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class OperationalTokenParameterException extends IllegalArgumentException {

    private ExpressionToken token;
    private String currentAction=null;
    private Stack<BaseDataMate> given;

    public OperationalTokenParameterException(ExpressionToken token, String currentAction) {
        this.token = token;
        this.currentAction = currentAction;
    }

    public OperationalTokenParameterException(ExpressionToken token,Stack<BaseDataMate> given, String currentAction) {
        this.token = token;
        this.given=given;
        this.currentAction = currentAction;
    }

    @Override
    public String getMessage() {
        StringBuffer message=new StringBuffer();

        message.append("Operational token \""+token.getTokenObject().getTokenString()+"\" (From "+token.getStartAt()+" to "+token.getEndAt()+") need parameters like following types:\n");

        OperationalToken operationalToken= (OperationalToken) token.getTokenObject();
        Map<String, IExecutableAction> actionMap=operationalToken.getAction().getActionMap();
        Set<String> keySet=actionMap.keySet();
        for(String key:keySet){
            message.append(key);
            message.append("\t");
        }

        if(given!=null && !given.isEmpty()){
            message.append("\nWhile given parameters are following\n");
            while(!given.isEmpty()){
                message.append(ExecutableActionUtils.getIdentifierByClass(ExecutableActionUtils.getTypeByDataMate(given.pop())));
                message.append("\t");
            }
        }


        if(currentAction!=null){
            message.append("\n");
            message.append("While doing action \""+currentAction+"\"");
        }

        return message.toString();
    }
}
