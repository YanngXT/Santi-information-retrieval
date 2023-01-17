package com.learn.pojo;

public class Sku {
    //章节标题
    private String title;
    //第几部
    private String version;
    //内容
    private String content;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getVersion() {
        return version;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Sku{" +
                "title='" + title + '\'' +
                ", version='" + version + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
