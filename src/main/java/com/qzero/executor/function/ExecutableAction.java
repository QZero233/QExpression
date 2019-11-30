package com.qzero.executor.function;

import java.util.HashMap;
import java.util.Map;

public class ExecutableAction {

    private Map<String,IExecutableAction> actionMap;

    private ExecutableAction(Map<String, IExecutableAction> actionMap) {
        this.actionMap = actionMap;
    }

    public IExecutableAction getAction(String parameterListIdentifier){
        IExecutableAction action=actionMap.get(parameterListIdentifier);
        return action;
    }

    public Map<String, IExecutableAction> getActionMap() {
        return actionMap;
    }

    public static class Builder{
        private Map<String,IExecutableAction> actionMap;

        public Builder() {
            actionMap=new HashMap<>();
        }

        public Builder(Map<String, IExecutableAction> actionMap) {
            if(actionMap==null)
                actionMap=new HashMap<>();
            this.actionMap = actionMap;
        }

        public Builder addAction(IExecutableAction action){
            //TODO ADD
            String identifier=ExecutableActionUtils.getParameterListIdentifier(action.getParametersType());
            actionMap.put(identifier,action);
            return this;
        }

        public ExecutableAction build(){
            return new ExecutableAction(actionMap);
        }
    }
}
