package com.qzero.executor;

import java.util.Date;

/**
 * Basic data mate to store data
 * @author QZero
 * @version 1.0
 */
public class BaseDataMate {

    public enum DataType {
        DATA_TYPE_STRING,
        DATA_TYPE_DOUBLE,
        DATA_TYPE_BOOLEAN,
        DATA_TYPE_DATE,
        DATA_TYPE_UNKNOWN
    }

    private DataType dataType;
    //The certain value of the constant it may be string or double
    private Object dataValue;

    public BaseDataMate(DataType constantType, Object constantValue) {
        if(constantValue==null && constantType!=DataType.DATA_TYPE_UNKNOWN)
            throw new IllegalArgumentException("Constant value can not be null");

        if(constantType== DataType.DATA_TYPE_STRING && !(constantValue instanceof String))
            throw new IllegalArgumentException("Constant type wrong,it's supposed to be String");

        if(constantType== DataType.DATA_TYPE_DOUBLE && !(constantValue instanceof Double))
            throw new IllegalArgumentException("Constant type wrong,it's supposed to be Double");

        if(constantType==DataType.DATA_TYPE_BOOLEAN && !(constantValue instanceof Boolean))
            throw new IllegalArgumentException("Constant type wrong,it's supposed to be Boolean");

        if(constantType==DataType.DATA_TYPE_DATE && !(constantValue instanceof Date))
            throw new IllegalArgumentException("Constant type wrong,it's supposed to be Date");

        if(constantType==DataType.DATA_TYPE_UNKNOWN && constantValue!=null)
            throw new IllegalArgumentException("Constant type wrong,it's supposed to be NULL");

        this.dataType=constantType;
        this.dataValue=constantValue;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Object getDataValue() {
        return dataValue;
    }

    @Override
    public String toString() {
        return "BaseDataMate{" +
                "dataType=" + dataType +
                ", dataValue=" + dataValue +
                '}';
    }
}
