package com.learn.controller;

import com.learn.pojo.QueryPojo;
import com.learn.pojo.ResultModel;
import com.learn.service.SearchService;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/list")//提交表单为list
public class SearchController {
    @Autowired
    private SearchService searchService;

    //由于前面有@Controller所以这里的String返回的是页面名
    @PostMapping("/sbkeshe")//本来应该写方法的路径，但是对应的页面中提交的路径就是前面RequestMapping的list，所以这里可以什么都不写。默认就是进到下面的query函数中去。
    public ResultModel query(@RequestBody QueryPojo queryPojo) throws Exception{
        //注意这几个变量名必须跟html中的变量属性值name完全一致
        String kind = queryPojo.getKind();
        Integer page = queryPojo.getPage();
        String version = queryPojo.getVersion();
        String queryString = queryPojo.getQueryString();
        //处理当前页
        if(StringUtils.isNullOrEmpty(String.valueOf(page))){
            page = 1;
        }
        if (page <= 0){
            page = 1;
        }

        //调用Service查询
        ResultModel resultModel = searchService.query(kind, queryString, version, page);
        System.out.println(queryPojo);
        System.out.println(resultModel);
        //查询条件回到页面。这几个变量名要和html中的value一致
        return resultModel;//把数据返回到这个页面
    }

}
