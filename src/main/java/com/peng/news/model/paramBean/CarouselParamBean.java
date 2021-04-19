package com.peng.news.model.paramBean;

import com.peng.news.util.ValidateUrlUtils;
import lombok.Data;

/**
 * 封装设置轮播的参数
 * @author PENG
 * @version 1.0
 * @date 2021/4/19 17:46
 */
@Data
public class CarouselParamBean {

    /**
     * 是否轮播
     */
    Boolean isCarousel;

    /**
     * 若设为轮播，则要设置轮播图片的地址
     */
    String imgUrlForCarousel;

    public void formatAndValidate() {
        format();
        validate();
    }

    private void format() {
        if(imgUrlForCarousel == null || "".equals(imgUrlForCarousel = imgUrlForCarousel.trim())) {
            imgUrlForCarousel = null;
        }
    }

    private void validate() {
        if(isCarousel == null) {
            throw new RuntimeException("请求出错，请正确设置请求参数！");
        }

        //如果设为轮播，则需要检查图片地址
        if(isCarousel) {
            if(imgUrlForCarousel == null) {
                throw new RuntimeException("轮播发布时，必须设置轮播图地址！");
            }else if(!ValidateUrlUtils.validateUrl(imgUrlForCarousel)) {
                throw new RuntimeException("图片地址不规范，请正确设置轮播图地址！");
            }
        }
    }
}
