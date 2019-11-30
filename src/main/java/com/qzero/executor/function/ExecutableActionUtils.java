package com.qzero.executor.function;

import com.qzero.executor.BaseDataMate;
import com.qzero.executor.exception.UnknownDataTypeException;

import java.util.Date;
import java.util.List;

public class ExecutableActionUtils {

    public static String getIdentifierByClass(Class type) {
        if (type == null)
            throw new IllegalArgumentException("type can not be null");

        if (type.equals(Double.class))
            return "D";
        else if (type.equals(String.class))
            return "S";
        else if (type.equals(Date.class))
            return "T";
        else if (type.equals(Boolean.class))
            return "B";
        else
            throw new UnknownDataTypeException(type.getSimpleName());

    }

    public static Class getTypeByDataMate(BaseDataMate dataMate){
        if(dataMate==null)
            return null;
        switch (dataMate.getDataType()){
            case DATA_TYPE_BOOLEAN:
                return Boolean.class;
            case DATA_TYPE_DOUBLE:
                return Double.class;
            case DATA_TYPE_DATE:
                return Date.class;
            case DATA_TYPE_STRING:
                return String.class;
            default:
                return null;
        }
    }

    /**
     * Get parameter list identifier by parameter type list
     * One higher letter means one type
     * Rules: Double-D,String-S,Date-T,Boolean-B
     *
     * @param parameterTypeList
     * @return
     */
    public static String getParameterListIdentifier(Class[] parameterTypeList) {
        if (parameterTypeList == null || parameterTypeList.length == 0)
            return "";

        StringBuffer result = new StringBuffer();
        for (Class type : parameterTypeList) {
            result.append(getIdentifierByClass(type));
        }

        return result.toString();
    }

    /**
     * Get parameter list identifier by parameter type list
     * One higher letter means one type
     * Rules: Double-D,String-S,Date-T,Boolean-B
     *
     * @param parameterTypeList
     * @return
     */
    public static String getParameterListIdentifier(List<Class> parameterTypeList) {
        if (parameterTypeList == null || parameterTypeList.isEmpty())
            return "";

        StringBuffer result = new StringBuffer();
        for (Class type : parameterTypeList) {
            result.append(getIdentifierByClass(type));
        }

        return result.toString();
    }

}
