package com.learn.pojo;

public class QueryPojo {
    private String kind;
    private String queryString;
    private String version;
    private Integer page;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "QueryPojo{" +
                "kind='" + kind + '\'' +
                ", queryString='" + queryString + '\'' +
                ", version='" + version + '\'' +
                ", page=" + page +
                '}';
    }
}
