package com.learn.service;

import com.learn.pojo.ResultModel;

public interface SearchService {
    //注意Service里面不应该返回页面了，应该返回数据
    //另外Model model是关于StringVC的，所以在service当中用不到。（前面的单词可能打错了没听清）
    public ResultModel query(String kind, String keyWord, String version, Integer page) throws Exception;
}
