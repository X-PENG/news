package com.peng.news.service.imp;

import com.peng.news.model.enums.NewsStatus;
import com.peng.news.model.paramBean.NewsBeanForInputterSave;
import com.peng.news.model.po.NewsPO;
import com.peng.news.service.NewsServiceForInputter;
import com.peng.news.util.UserUtils;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/12 21:26
 */
public abstract class AbstractNewsServiceForInputter implements NewsServiceForInputter {

    @Override
    public Integer saveNewsAsDraftOrUpload(int tag, NewsBeanForInputterSave news) {
        //先对新闻信息进行格式化
        news.trimOrSetNull();
        //再对信息进行校验
        validateNewsInfo(news);
        Integer newsId = news.getId();

        //新闻存在外链的话，其他信息都不保存，设为null
        if(news.getExternalUrl() != null) {
            news.setContent(null);
            news.setImgSource(null);
            news.setArticleSource(null);
        }

        //不为null，则可能要更新新闻
        if(newsId != null){
            //确保传稿人更新的新闻存在，且是当前请求用户的草稿
            assertNewsExistsAndBelongCurUserDraft(newsId);
        }

        if(tag == 1){
            return newsId == null ? createNewsAsDraft(news) : saveAsDraft(news);
        }else if(tag == 2){
            return  newsId == null ? createNewsAsCompleted(news) : saveAsCompleted(news);
        }
        throw new RuntimeException("不支持此操作！");
    }


    /**
     * 确保id为newsId的新闻存在，且该新闻是当前请求用户的草稿
     * 不存在这样的新闻，就报错；存在，就返回。
     * 该方法保证传稿人只能操作自己创建的草稿
     * @param newsId 新闻id
     * @return 新闻对象
     */
    protected NewsPO assertNewsExistsAndBelongCurUserDraft(Integer newsId) {
        NewsPO selectNews = assertNewsExists(newsId);
        if(!UserUtils.getUser().getId().equals(selectNews.getInputterId())){
            throw new RuntimeException("不能操作他人创建的新闻，操作失败！");
        }
        Integer oldStatus = selectNews.getNewsStatus();
        //状态必须是草稿，才能保存；否则，异常
        if(oldStatus != NewsStatus.DRAFT.getCode()){
            throw new RuntimeException("状态异常，新闻当前状态为【" + NewsStatus.fromCode(oldStatus).getName() + "】，操作失败！");
        }

        return selectNews;
    }

    /**
     * 对保存的新闻信息进行校验
     * @param news
     */
    private void validateNewsInfo(NewsBeanForInputterSave news) {
        validateNewsInfoIsComplete(news);
        validateNewsInfoIsLegality(news);
    }

    /**
     * 完整性校验
     * @param news
     */
    private void validateNewsInfoIsComplete(NewsBeanForInputterSave news){
        if(news.getTitle() == null) {
            throw new RuntimeException("新闻标题不能为空！");
        }
    }

    /**
     * 合法性校验
     * @param news
     */
    private void validateNewsInfoIsLegality(NewsBeanForInputterSave news){

    }

    /**
     * 保证id为newsId的新闻存在，若不存在则报错；存在，就返回新闻
     * @param newsId 新闻id
     * @return 新闻对象
     */
    protected abstract NewsPO assertNewsExists(Integer newsId);

    /**
     * 创建新闻并设为草稿
     * @param news 用户填写的新闻信息
     * @return 新闻id
     */
    protected abstract Integer createNewsAsDraft(NewsBeanForInputterSave news);

    /**
     * 创建新闻并将状态设为完成录入的状态（即上传成功）
     * @param news 用户填写的新闻信息
     * @return 新闻id
     */
    protected abstract Integer createNewsAsCompleted(NewsBeanForInputterSave news);


    /**
     * 保存新闻，状态设为草稿
     * @param news
     * @return
     */
    protected abstract Integer saveAsDraft(NewsBeanForInputterSave news);

    /**
     * 保存新闻，状态设为上传成功
     * @param news
     * @return
     */
    protected abstract Integer saveAsCompleted(NewsBeanForInputterSave news);
}
