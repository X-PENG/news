package com.peng.news.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.peng.news.model.dto.ReModificationInfoDTO;
import com.peng.news.model.dto.ReviewFailInfoDTO;
import com.peng.news.model.po.UserPO;
import com.peng.news.util.Constants;
import com.peng.news.util.DateTimeFormatUtils;
import com.peng.news.util.JSONObjectConvertJavaBeanUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/15 17:29
 */
public class JSONObjectConvertJavaBeanUtilsTest {

    @Test
    public void t1() {
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
        ReModificationInfoDTO convertResult = JSONObjectConvertJavaBeanUtils.convert(jsonObject, ReModificationInfoDTO.class);

        Assert.assertEquals(convertResult.getOperateTime(), reModificationInfoDTO.getOperateTime());
        Assert.assertEquals(convertResult.getSuggestion(), reModificationInfoDTO.getSuggestion());
        Assert.assertEquals(convertResult.getOperator().getId(), reModificationInfoDTO.getOperator().getId());
    }

    @Test
    public void t2() {
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

        Map parseObject = JSON.parseObject(jsonStr2, Map.class);
        JSONObject jsonObject = ((JSONObject) parseObject.get(Constants.REVIEW_FAIL_KEY));
        ReviewFailInfoDTO convertAssert = JSONObjectConvertJavaBeanUtils.convert(jsonObject, ReviewFailInfoDTO.class);
        Assert.assertEquals(convertAssert.getEpoch(), reviewFailInfoDTO.getEpoch());
        Assert.assertEquals(convertAssert.getReviewer().getId(), reviewFailInfoDTO.getReviewer().getId());
        Assert.assertEquals(convertAssert.getReviewTime(), reviewFailInfoDTO.getReviewTime());
        Assert.assertEquals(convertAssert.getSuggestion(), reviewFailInfoDTO.getSuggestion());
    }
}
