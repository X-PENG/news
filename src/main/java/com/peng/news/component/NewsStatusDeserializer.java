package com.peng.news.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.peng.news.model.enums.NewsStatus;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * 自定义NewsStatus反序列化器
 * @author PENG
 * @version 1.0
 * @date 2021/6/26 9:25
 */
public class NewsStatusDeserializer extends StdDeserializer<NewsStatus> {
    public NewsStatusDeserializer() {
        super(NewsStatus.class);
    }
    @Override
    public NewsStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        while (!p.isClosed()) {
            JsonToken curToken = p.nextToken();
            //如果遇到了id字段
            if(JsonToken.FIELD_NAME.equals(curToken) && "code".equals(p.getCurrentName())) {
                JsonToken valueToken = p.nextToken();
                Assert.isTrue(JsonToken.VALUE_NUMBER_INT.equals(valueToken), "NewsStatus反序列化异常");
                int code = p.getValueAsInt();
                return NewsStatus.fromCode(code);
            }
        }

        return null;
    }
}
