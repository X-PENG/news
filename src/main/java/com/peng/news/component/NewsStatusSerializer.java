package com.peng.news.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.peng.news.model.enums.NewsStatus;

import java.io.IOException;

/**
 * 自定义NewsStatus序列化器
 * @author PENG
 * @version 1.0
 * @date 2021/6/26 10:41
 */
public class NewsStatusSerializer extends StdSerializer<NewsStatus> {

    public NewsStatusSerializer() {
        super(NewsStatus.class);
    }

    @Override
    public void serialize(NewsStatus value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if(value == null) {
            gen.writeNull();
            return;
        }

        gen.writeStartObject();
        gen.writeNumberField("code", value.getCode());
        gen.writeStringField("name", value.getName());
        gen.writeEndObject();
    }
}
