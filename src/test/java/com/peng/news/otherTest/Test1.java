package com.peng.news.otherTest;

import com.peng.news.model.paramBean.NewsBeanForInputterSave;
import com.peng.news.model.po.NewsPO;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/26 19:58
 */
public class Test1 {

    @Test
    public void t1(){
        NewsBeanForInputterSave newsBeanForInputterSave = new NewsBeanForInputterSave();
        newsBeanForInputterSave.setId(1);
        newsBeanForInputterSave.setTitle("setTitle");
        newsBeanForInputterSave.setContent("setContent");
        newsBeanForInputterSave.setImgSource("setImgSource");
        newsBeanForInputterSave.setArticleSource("setArticleSource");
        newsBeanForInputterSave.setExternalUrl("setExternalUrl");

        NewsPO newsPO = new NewsPO();
        BeanUtils.copyProperties(newsBeanForInputterSave, newsPO);
        System.out.println(newsPO);
    }
}
