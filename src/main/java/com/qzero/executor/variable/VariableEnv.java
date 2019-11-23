package com.qzero.executor.variable;

import com.qzero.executor.BaseDataMate;

import java.util.HashMap;
import java.util.Map;

public class VariableEnv {

    private Map<String, BaseDataMate> variableMap=new HashMap<>();

    public VariableEnv(Map<String, BaseDataMate> variableMap) {
        this.variableMap = variableMap;
    }

    public BaseDataMate getVariableValue(String variableName){
        if(variableMap==null)
            return null;
        return variableMap.get(variableName);
    }

    public void addOrEditVariable(String variableName,BaseDataMate value){
        variableMap.put(variableName,value);
    }

    public void removeVariable(String variableName){
        variableMap.remove(variableName);
    }

    public void clearVariable(){
        variableMap.clear();
    }

    @Override
    public String toString() {
        return "VariableEnv{" +
                "variableMap=" + variableMap +
                '}';
    }
}
