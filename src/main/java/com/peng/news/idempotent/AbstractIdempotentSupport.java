package com.peng.news.idempotent;

import com.peng.news.util.UserUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/6/27 19:06
 */
public abstract class AbstractIdempotentSupport implements IdempotentSupport {
    /**
     * 16把锁，为了降低锁的粒度
     */
    ReentrantLock[] locks = new ReentrantLock[16];

    public AbstractIdempotentSupport() {
        //初始化锁
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    @Override
    public boolean isRepeatRequest(HttpServletRequest request) {
        String convertKey = convertKey(request);
        ReentrantLock curUserLock = getCurUserLock();
        //加锁。判断痕迹+留痕必须是原子操作
        curUserLock.lock();
        try{
            if(hasMark(convertKey)) {
                return true;
            }
            leaveMark(convertKey);
            return false;
        }finally {
            curUserLock.unlock();
        }
    }

    /**
     * 获取当前用户的锁。（降低锁的粒度）
     * @return
     */
    private ReentrantLock getCurUserLock() {
        Integer userId = UserUtils.getUser().getId();
        return locks[userId % locks.length];
    }


    /**
     * 将request 转成 一个string类型的key
     * @param request
     * @return
     */
    protected abstract String convertKey(HttpServletRequest request);

    /**
     * 根据key判断请求是否已经留下痕迹
     * @param key
     * @return
     */
    protected abstract boolean hasMark(String key);

    /**
     * 请求留痕，用于判断是否重复请求
     * @param key
     */
    protected abstract void leaveMark(String key);
}
