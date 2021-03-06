package com.xcphoenix.dto.utils.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/14 上午9:27
 */
public class SqlTimeSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        final String separator = ":";
        final int indexOfSeparator = 5;

        SerializeWriter out = serializer.out;

        String sqlTime = object.toString();
        if (sqlTime == null) {
            out.writeString("");
            return;
        }
        if (sqlTime.lastIndexOf(separator) == indexOfSeparator) {
            out.writeString(sqlTime.substring(0, indexOfSeparator));
        } else {
            out.writeString(sqlTime);
        }
    }
}
