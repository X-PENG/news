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
 * @date 2021/4/15 14:52
 */
public class ReviewFailInfoTest {

    @Test
    public void t1(){
        HashMap<String, Object> map = new HashMap<>();
        ReviewFailInfoDTO reviewFailInfoDTO = new ReviewFailInfoDTO();
        UserPO reviewer = new UserPO();
        reviewer.setId(1);
        reviewFailInfoDTO.setReviewer(reviewer);
        reviewFailInfoDTO.setEpoch(1);
        reviewFailInfoDTO.setReviewTime(DateTimeFormatUtils.format(LocalDateTime.now()));
        reviewFailInfoDTO.setSuggestion("第一段第二句话重新写");
        String jsonStr1 = JSON.toJSONString(reviewFailInfoDTO);

        map.put(Constants.REVIEW_FAIL_KEY, reviewFailInfoDTO);
        String jsonStr2 = JSON.toJSONString(map);
        System.out.println(jsonStr2);
        System.out.println(jsonStr2.length());

        Map parseObject = JSON.parseObject(jsonStr2, Map.class);
        JSONObject jsonObject = ((JSONObject) parseObject.get(Constants.REVIEW_FAIL_KEY));
        Assert.assertEquals(((JSONObject) jsonObject.get("reviewer")).get("id"), reviewer.getId());
        Assert.assertEquals(jsonObject.get("epoch"), reviewFailInfoDTO.getEpoch());
        Assert.assertEquals(jsonObject.get("reviewTime"), reviewFailInfoDTO.getReviewTime());
        Assert.assertEquals(jsonObject.get("suggestion"), reviewFailInfoDTO.getSuggestion());

        System.out.println(jsonObject.getClass());
        System.out.println(JSON.toJSONString(jsonObject));
    }

    @Test
    public void t2(){
        HashMap map = new HashMap();
        map.put("ceshi","测试");
        String jsonString = JSON.toJSONString(map);

        Map map1 = JSON.parseObject(jsonString, Map.class);
        Assert.assertNull(map1.get(Constants.REVIEW_FAIL_KEY));
        Assert.assertNull(map1.get(Constants.RE_MODIFICATION_KEY));
    }
}
