package com.peng.news.model.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.peng.news.model.po.UserPO;
import com.peng.news.util.Constants;
import com.peng.news.util.DateTimeFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/15 15:15
 */
public class ReModificationInfoTest {

    @Test
    public void t1(){
        ReModificationInfoDTO reModificationInfoDTO = new ReModificationInfoDTO();
        reModificationInfoDTO.setOperateTime(DateTimeFormatUtils.format(LocalDateTime.now()));
        reModificationInfoDTO.setSuggestion("重新修改一下");
        UserPO operator = new UserPO();
        operator.setId(1);
        reModificationInfoDTO.setOperator(operator);
        System.out.println(JSON.toJSONString(reModificationInfoDTO));

        Map map = new HashMap();
        map.put(Constants.RE_MODIFICATION_KEY, reModificationInfoDTO);
        String jsonStr1 = JSON.toJSONString(map);
        System.out.println(jsonStr1);

        Map map1 = JSON.parseObject(jsonStr1, Map.class);
        JSONObject jsonObject = (JSONObject) map1.get(Constants.RE_MODIFICATION_KEY);
        Assert.assertEquals(jsonObject.get("operateTime"), reModificationInfoDTO.getOperateTime());
        Assert.assertEquals(jsonObject.get("suggestion"), reModificationInfoDTO.getSuggestion());
        Assert.assertEquals(((JSONObject) jsonObject.get("operator")).get("id"), operator.getId());

        System.out.println(JSON.toJSONString(jsonObject));
    }

    @Test
    public void t2(){
        ReModificationInfoDTO reModificationInfoDTO = new ReModificationInfoDTO();
        reModificationInfoDTO.setOperateTime(DateTimeFormatUtils.format(LocalDateTime.now()));
        reModificationInfoDTO.setSuggestion("重新修改一下");
        UserPO operator = new UserPO();
        operator.setId(1);
        reModificationInfoDTO.setOperator(operator);

        Map map = new HashMap();
        map.put(Constants.RE_MODIFICATION_KEY, reModificationInfoDTO);
        String jsonStr1 = JSON.toJSONString(map);
        System.out.println(jsonStr1);

    }
}
