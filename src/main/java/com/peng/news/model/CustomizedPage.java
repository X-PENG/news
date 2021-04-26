package com.peng.news.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/3/25 19:13
 */
@Data
public class CustomizedPage<T> {
    List<T> records = new ArrayList<>();
    long total;
    long current;
    long size;

    public CustomizedPage() {
    }

    public static <T> CustomizedPage<T> fromIPage(IPage<T> page){
        CustomizedPage<T> customizedPage = new CustomizedPage<>();
        customizedPage.setCurrent(page.getCurrent());
        customizedPage.setSize(page.getSize());
        customizedPage.setTotal(page.getTotal());
        customizedPage.setRecords(page.getRecords());
        return customizedPage;
    }

    /**
     * 返回空数据页
     * @param <T>
     * @return
     */
    public static <T> CustomizedPage<T> emptyPage(Class<T> clazz) {
        CustomizedPage<T> customizedPage = new CustomizedPage<T>();
        customizedPage.setCurrent(1);
        customizedPage.setSize(10);
        return customizedPage;
    }
}
