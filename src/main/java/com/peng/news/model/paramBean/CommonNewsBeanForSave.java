package com.peng.news.model.paramBean;

import lombok.Data;

/**
 * 为传稿人、编辑和审核人员保存新闻（创建或更新）准备的公共bean
 * @author PENG
 * @version 1.0
 * @date 2021/4/14 14:45
 */
@Data
public abstract class CommonNewsBeanForSave {
    /**
     * 新闻id
     */
    protected Integer id;

    /**
     * 标题
     */
    protected String title;

    /**
     * 用于新闻列表显示的文章片段
     */
    String articleFragmentForShow;

    /**
     * 正文
     */
    protected String content;

    /**
     * 新闻图片来源
     */
    protected String imgSource;

    /**
     * 文字来源
     */
    protected String articleSource;

    /**
     * 新闻外链
     */
    protected String externalUrl;

    /**
     * 对信息进行格式化并且校验新闻信息，包括：合法校验和完整性校验
     */
    public void formatAndValidate() {
        format();
        validate();
    }

    /**
     * 格式化规则：
     * 1.对字符串两边空格进行修剪；
     * 2.空字符串设为null；
     * 3.如果新闻设置了外链，除了title之外其他信息都不保存，设为null
     */
    private void format() {
        trimOrSetNull();

        //存在外链的话，title之外的其他信息都不保存，设为null
        if(externalUrl != null) {
            content = null;
            imgSource = null;
            articleSource = null;
        }
    }

    /**
     * 对字符串两边空格进行修剪，如果是空字符串，就设为null
     * @return
     */
    private void trimOrSetNull(){
        if(title == null || "".equals(title = title.trim())) {
            title = null;
        }

        if(imgSource == null || "".equals(imgSource = imgSource.trim())) {
            imgSource = null;
        }

        if(articleSource == null || "".equals(articleSource = articleSource.trim())) {
            articleSource = null;
        }

        if(externalUrl == null || "".equals(externalUrl = externalUrl.trim())) {
            externalUrl = null;
        }
    }

    private void validate () {
        validateIsComplete();
        validateIsLegality();
    }

    /**
     * 完整性校验
     */
    private void validateIsComplete(){
        if(title == null) {
            throw new RuntimeException("新闻标题不能为空！");
        }
    }

    /**
     * 合法性校验
     */
    private void validateIsLegality(){

    }
}
