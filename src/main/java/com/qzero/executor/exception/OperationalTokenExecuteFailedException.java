package com.qzero.executor.exception;

import com.qzero.executor.BaseDataMate;
import com.qzero.executor.ExpressionToken;
import com.qzero.executor.function.ExecutableActionUtils;

public class OperationalTokenExecuteFailedException extends IllegalStateException {

    private ExpressionToken token;
    private Class[] currentParameterTypeList;
    private BaseDataMate[] parameters;

    public OperationalTokenExecuteFailedException(ExpressionToken token, Class[] currentParameterTypeList, BaseDataMate[] parameters) {
        this.token = token;
        this.currentParameterTypeList = currentParameterTypeList;
        this.parameters = parameters;
    }

    @Override
    public String getMessage() {
        StringBuffer message=new StringBuffer();

        message.append("Execute operational token \""+token.getTokenObject().getTokenString()+"\"(From "+token.getStartAt()+" to "+token.getEndAt()+" ) failed\n");

        String identifier= ExecutableActionUtils.getParameterListIdentifier(currentParameterTypeList);
        message.append("Current parameter type list is following:\n");
        message.append(identifier);
        message.append("\nCurrent parameters are following:\n");

        for(BaseDataMate dataMate:parameters){
            message.append(dataMate.toString());
            message.append("\t");
        }


        return message.toString();
    }
}
