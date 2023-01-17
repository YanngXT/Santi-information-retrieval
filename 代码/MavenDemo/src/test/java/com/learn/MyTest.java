package com.learn;

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
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MyTest {

    @Test
    public void query() throws Exception {
        String kind="title";
        String keyWord="叶文洁";
        String version="三体1";
        Integer page=1;
        Integer PAGE_SIZE = 10;
        //1.需要使用的对象封装
        ResultModel resultModel = new ResultModel();
        //从第几条开始查询
        int start = (page + 1) * PAGE_SIZE;
        //查询多少条
        Integer end = page * PAGE_SIZE;
        // 1. 创建Query搜索对象
        // 创建分词器
        Analyzer analyzer = new IKAnalyzer();//IKAnalyzer()

        Query query1 = null;
        if(kind.equals("content")){//通过文章内容进行查找
            // 创建搜索解析器，第一个参数：默认Field域，第二个参数：分词器
            QueryParser queryParser1 = new QueryParser("content", analyzer);
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
        Directory directory = FSDirectory.open(Paths.get("D:\\ChapterSearchDir"));
        // 3. 创建索引读取对象IndexReader
        IndexReader reader = DirectoryReader.open(directory);
        // 4. 创建索引搜索对象
        IndexSearcher searcher = new IndexSearcher(reader);
        // 5. 使用索引搜索对象，执行搜索，返回结果集TopDocs
        // 第一个参数：搜索对象，第二个参数：返回的数据条数，指定查询结果最顶部的n条数据返回
        TopDocs topDocs = searcher.search(query.build(), 50);
        System.out.println("查询到的数据总条数是：" + topDocs.totalHits);
        // 获取查询结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //10.遍历结果集，封装返回的数据
        List<Sku> skuList = new ArrayList<>();
        if(scoreDocs != null){
            List<String> titles = new ArrayList<String>();
            //因为lucene不提供这种分页式的查询，所以这里手动控制
            for (int i = start; i < end;) {//注意这里是<不是<=
                //通过查询到的文档编号，找到对应的文档对象
                Document document = reader.document(scoreDocs[i].doc);;
                //去重操作
                if(titles.contains(document.get("title"))){
                    continue;
                }else {
                    titles.add(document.get("title"));
                    //封装Sku对象
                    Sku sku = new Sku();
                    sku.setVersion(document.get("version"));//这里根据域名来写
                    sku.setTitle(document.get("title"));
                    sku.setContent(document.get("content"));
                    i++;
                    skuList.add(sku);
                }
            }
        }
        //封装查询到的结果组
        resultModel.setSkuList(skuList);
        //封装当前页
        resultModel.setCurPage(Long.parseLong(String.valueOf(page)));
        //总页数
        Long pageCount = topDocs.totalHits % PAGE_SIZE > 0 ? (topDocs.totalHits / PAGE_SIZE + 1) : (topDocs.totalHits / PAGE_SIZE);
        resultModel.setPageCount(pageCount);
//        return resultModel;
        System.out.println(resultModel);
    }
}
