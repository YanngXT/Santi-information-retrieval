package com.learn.pojo;

import java.util.List;

/**
 * 自定义分页实体类
 */

public class ResultModel {
    //文章列表
    private List<Sku> skuList;
    //文章总数
    private Long recordCount;
    //总页数
    private Long pageCount;
    //当前页
    private Long curPage;
    //单词总数
    private Long wordCount;
    //查询用时
    private Long consumeTime;

    public Long getConsumeTime() { return consumeTime; }

    public void setConsumeTime(Long consumeTime) { this.consumeTime = consumeTime; }

    public void setWordCount(Long wordCount) {
        this.wordCount = wordCount;
    }

    public Long getWordCount() {
        return wordCount;
    }


    public List<Sku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public Long getPageCount() {
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public Long getCurPage() {
        return curPage;
    }

    public void setCurPage(Long curPage) {
        this.curPage = curPage;
    }

    @Override
    public String toString() {
        return "ResultModel{" +
                "skuList=" + skuList +
                ", recordCount=" + recordCount +
                ", pageCount=" + pageCount +
                ", curPage=" + curPage +
                ", wordCount=" + wordCount +
                ", consumeTime=" + consumeTime +
                '}';
    }
}
