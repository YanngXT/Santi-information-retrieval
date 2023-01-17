package com.learn;

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
import java.util.Arrays;
import java.util.List;

public class TestSearch {
    @Test
    public void testIndexTextSearch() throws Exception {
        // 1. 创建Query搜索对象
        // 创建分词器
        Analyzer analyzer = new IKAnalyzer();
        // 创建搜索解析器，第一个参数：默认Field域，第二个参数：分词器
        QueryParser queryParser = new QueryParser("contents", analyzer);
        // 创建搜索对象。注意QueryParser只能对文本进行查找
        Query query = queryParser.parse("叶文洁");
        // 2. 创建Directory流对象,声明索引库位置
        Directory directory = FSDirectory.open(Paths.get("D:\\ChapterSearchDir"));
        // 3. 创建索引读取对象IndexReader
        IndexReader reader = DirectoryReader.open(directory);
        // 4. 创建索引搜索对象
        IndexSearcher searcher = new IndexSearcher(reader);
        // 5. 使用索引搜索对象，执行搜索，返回结果集TopDocs
        // 第一个参数：搜索对象，第二个参数：返回的数据条数，指定查询结果最顶部的n条数据返回
        TopDocs topDocs = searcher.search(query, 50); System.out.println("查询到的数据总条数是：" + topDocs.totalHits);
        // 获取查询结果集
        ScoreDoc[] docs = topDocs.scoreDocs;
        // 6. 解析结果集
        for (ScoreDoc scoreDoc : docs) {
            // 获取文档
            int docID = scoreDoc.doc;
            Document doc = searcher.doc(docID);
            System.out.println("=============================");
            System.out.println("version:" + doc.get("version"));
            System.out.println("title:" + doc.get("title"));
            System.out.println("content:" + doc.get("contents"));
        }
        // 7. 释放资源
        reader.close();
    }

    public static boolean useList(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    @Test
    public void testIndexCombinationSearch() throws Exception {
        /**
         * When there is only one key word, it means that we search in titles
         * @param keyWord: (default=null)关键词
         * @param version: (default=null)三体版本
         * @param kind: (default="title")查找范围
         *
         */
        // 1. 创建Query搜索对象
        // 创建分词器
        Analyzer analyzer = new IKAnalyzer();
        String kind = "content";
        String keyWord = "世界";
        String version = "三体3";

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

        /**把两个类型的查询融合在一起,
         * BooleanClause.Occur.MUST是AND，
         * BooleanClause.Occur.SHOULD是OR，
         * BooleanClause.Occur.MUST_NOT相当于NOT
         * 注意，当两个都是MUST_NOT或者只有一个查询且该查询是MUST_NOT，则查询不出任何结果。
         * */
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
        System.out.println("查询到的数据总条数是：" + topDocs.totalHits);
        // 获取查询结果集
        ScoreDoc[] docs = topDocs.scoreDocs;
        // 6. 解析结果集
        List<String> titles = new ArrayList<String>();
        for (ScoreDoc scoreDoc : docs) {
            // 获取文档
            int docID = scoreDoc.doc;
            Document doc = searcher.doc(docID);
            if(titles.contains(doc.get("title"))){
                continue;
            }else {
                titles.add(doc.get("title"));
                System.out.println("=============================");
                System.out.println("version:" + doc.get("version"));
                System.out.println("title:" + doc.get("title"));
                System.out.println("content:" + doc.get("contents"));
            }
        }
        // 7. 释放资源
        reader.close();
    }
}
