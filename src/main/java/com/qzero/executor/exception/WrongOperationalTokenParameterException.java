package com.qzero.executor.exception;

import com.qzero.executor.ExpressionToken;
import com.qzero.executor.token.ExecutableAction;

public class WrongOperationalTokenParameterException extends IllegalArgumentException {

    private String message;
    private ExpressionToken token;


    /**
     * For parameter count exception
     */
    public WrongOperationalTokenParameterException(ExpressionToken token, ExecutableAction action) {
        super();
        this.token=token;
        message="Wrong operational token parameter when compiling operational token \""+token.getTokenObject().getTokenString()+"\"\n";
        message+="The count of parameters is wrong,it should be "+action.getParameterCount();
        message+= "Wrong token start at "+token.getStartAt()+" and end at "+token.getEndAt();
    }

    /**
     * For parameter type exception
     */
    public WrongOperationalTokenParameterException(ExpressionToken token,Class required,Class given,int index) {
        super();
        this.token=token;
        message="Wrong operational token parameter when compiling operational token \""+token.getTokenObject().getTokenString()+"\"\n";
        message+= "The number "+(index+1)+" parameter "+required.getSimpleName()+" is required while "+given.getSimpleName()+" is given\n";
        message+= "Wrong token start at "+token.getStartAt()+" and end at "+token.getEndAt();
    }

    public String getMessage() {
        return message;
    }

    public ExpressionToken getToken() {
        return token;
    }

}
