package com.learn.service;
/**一定要注意导入包问题，千万别弄错Analyzer的来源*/

import com.learn.pojo.ResultModel;
import com.learn.pojo.Sku;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService{
    //每页查询20条数据，封装成了常量
    public final static Integer PAGE_SIZE = 10;
//    public final static Integer PAGE_SIZE = 10000;
    @Override
    public ResultModel query(String kind, String keyWord, String version, Integer page) throws Exception {
        //新加的查询用时
        Date startTime = new Date();

        //1.需要使用的对象封装
        ResultModel resultModel = new ResultModel();
        //从第几条开始查询（page从1开始）
        int start = (page-1) * PAGE_SIZE;
        //查询多少条
        int end = page * PAGE_SIZE;
//        System.out.println("page: " + page + " start: " + start + " end:" + end);
        // 1. 创建Query搜索对象
        // 创建分词器
        Analyzer analyzer = new IKAnalyzer();//IKAnalyzer()

        Query query1 = null;
        if(kind.equals("content")){//通过文章内容进行查找
            // 创建搜索解析器，第一个参数：默认Field域，第二个参数：分词器
            QueryParser queryParser1 = new QueryParser("contents", analyzer);
            // 创建搜索对象
            query1 = queryParser1.parse(keyWord);
        }else{//通过文章题目进行查找
            // 创建搜索解析器，第一个参数：默认Field域，第二个参数：分词器
            QueryParser queryParser1 = new QueryParser("title", analyzer);
            // 创建搜索对象
            query1 = queryParser1.parse(keyWord);
        }

        QueryParser queryParser2 = new QueryParser("version", analyzer);
        Query query2 = queryParser2.parse(version);

        //创建组合查询对象（布尔查询对象）
        BooleanQuery.Builder query = new BooleanQuery.Builder();
        query.add(query1, BooleanClause.Occur.MUST);
        query.add(query2, BooleanClause.Occur.MUST);

        // 2. 创建Directory流对象,声明索引库位置
        Directory directory = FSDirectory.open(Paths.get("D:\\ChapterSearchDir2"));
        // 3. 创建索引读取对象IndexReader
        IndexReader reader = DirectoryReader.open(directory);
        // 4. 创建索引搜索对象
        IndexSearcher searcher = new IndexSearcher(reader);
        // 5. 使用索引搜索对象，执行搜索，返回结果集TopDocs
        // 第一个参数：搜索对象，第二个参数：返回的数据条数，指定查询结果最顶部的n条数据返回
        TopDocs topDocs = searcher.search(query.build(), 5000);
//        System.out.println("查询到的数据总条数是：" + topDocs.totalHits);
        // 获取查询结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //10.遍历结果集，封装返回的数据
        List<Sku> skuList = new ArrayList<>();
        Long articles = 0L;
        if(scoreDocs != null){
            //首先把题目和内容全部存下来
            List<String> titles = new ArrayList<String>();
            List<String> contents = new ArrayList<String>();
//            System.out.println(" " + topDocs.totalHits + " " + scoreDocs.length);
            for (int i = 0; i < topDocs.totalHits; i++){
                //通过查询到的文档编号，找到对应的文档对象
                Document document = reader.document(scoreDocs[i].doc);;
                //去重操作
                if(titles.contains(document.get("title"))){
                    continue;
                }else{
                    titles.add(document.get("title"));
                    contents.add(document.get("contents"));
//                    System.out.println("Add to titles and contents");
                }
            }
            articles = Long.valueOf(contents.size());
//            System.out.println(" " + titles.size() + " " + contents.size());
            //然后按照题目顺序进行skuList放入添加
            //因为lucene不提供这种分页式的查询，所以这里手动控制
//            System.out.println(start);
            for (int i = start; i < end && i < contents.size(); i++) {//注意这里是<不是<=
                //封装Sku对象
                Sku sku = new Sku();
                sku.setVersion(version);//这里根据域名来写
                sku.setTitle(titles.get(i));
                sku.setContent(contents.get(i));
                skuList.add(sku);
//                System.out.println("Successfully added");
            }
        }
        //新加的查询用时
        Date endTime = new Date();
//        System.out.println(skuList);
        //封装查询到的结果组
        resultModel.setSkuList(skuList);
        //封装当前页
        resultModel.setCurPage(Long.parseLong(String.valueOf(page)));
        //总页数
        Long pageCount = Long.valueOf(articles / PAGE_SIZE);
        if (articles / PAGE_SIZE * PAGE_SIZE < articles){
            resultModel.setPageCount(pageCount + 1);
        }else{
            resultModel.setPageCount(pageCount);
        }
        resultModel.setRecordCount( articles );//(topDocs.totalHits);
        resultModel.setWordCount(topDocs.totalHits);
        resultModel.setConsumeTime(endTime.getTime() - startTime.getTime());
        System.out.println("I', backing");
        return resultModel;
    }
}
