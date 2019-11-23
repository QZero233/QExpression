package com.qzero.executor.token;

import com.qzero.executor.BaseDataMate;

/**
 * Class of constant token
 * @author QZero
 * @version 1.0
 */
public class ConstantToken extends TokenObject {

    private BaseDataMate dataMate;

    public ConstantToken(String tokenString, BaseDataMate.DataType dataType,Object dataValue) {
        super(tokenString);
        this.dataMate=new BaseDataMate(dataType,dataValue);
    }

    public BaseDataMate getDataMate() {
        return dataMate;
    }

    public void setDataMate(BaseDataMate dataMate) {
        this.dataMate = dataMate;
    }

    @Override
    public String toString() {
        return "ConstantToken{" +
                "dataMate=" + dataMate +
                ", tokenString='" + tokenString + '\'' +
                '}';
    }
}
