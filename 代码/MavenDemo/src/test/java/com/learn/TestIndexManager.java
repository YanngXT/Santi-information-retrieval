package com.learn;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
 * 索引库维护
 * */
public class TestIndexManager{
    /*
     * 创建索引库
     * */
    public List<Document> getMyDocs(String path)throws Exception{
        File[] files = new File(path).listFiles();
        List<Document> docList = new ArrayList<>();
        //好离谱啊又有点好笑这个截取反斜杠，是因为java和regex的双重机制罢了
        //D:\0学习\数据检索\爬取三体数据\小说\三体1\174 三体1：地球往事 1.疯狂年代.txt
        //三体1
        String[] tmp = path.split("\\\\");
        System.out.println(tmp);
        String version = tmp[tmp.length - 1];

        for(int i = 0; i < files.length; i++){
            //获取文件名称 "174 三体1：地球往事 1.疯狂年代.txt"
            //1.疯狂年代.txt
            //txt
            //1.疯狂年代
            String [] tmp2 = files[i].getName().split(" ", -1);
            String fileName = tmp2[tmp2.length - 1];
            //获取文件后缀名，将其作为文件类型
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1,
                    fileName.length()).toLowerCase();
            String title = fileName.substring(0,
                    fileName.lastIndexOf(".")+1).toLowerCase();
            Document doc = new Document();

            InputStream in = new FileInputStream(files[i]);
            InputStreamReader reader = null;

            if(fileType != null && !fileType.equals("")){
                if(fileType.equals("txt")){
                    //建立一个输入流对象
                    reader = new InputStreamReader(in);
                    //建立一个对象，他把文件内容转成计算机能读懂的语言
                    BufferedReader br = new BufferedReader(reader);
                    String txtFile = "";
                    String line = null;
                    if(title.endsWith(".")){
                        System.out.println(".title: " + title.substring(0, title.length()-1) + "  version: " + version);
                    }else{
                        System.out.println("title: " + title + "version: " + version);
                    }

                    while((line = br.readLine()) != null){
                        //一次读入一行数据
                        txtFile += line + '\n';
                    }
                    //创建Field对象，并放入doc对象中
                    /**
                     * 是否分词：是，因为内容字段需要查询
                     * 是否索引：是，因为需要根据内容字段进行查询
                     * 是否存储：是，因为页面需要展示部分内容，所以需要存储
                     */
//                    System.out.println(txtFile);
                    doc.add(new TextField("contents", txtFile, Field.Store.YES));
//                    doc.add(new Field("contents", txtFile, Field.Store.YES, Field.Index.ANALYZED));
                    /**
                     * 是否分词：否，因为分类是专有名词，是一个整体，所以不分词。
                     * 是否索引：是，因为需要根据分类查询。
                     * 是否存储：是，因为页面需要展示分类。
                     */
                    doc.add(new StringField("version",version, Field.Store.YES));
                    /**
                     * 是否分词：是，因为名称字段需要查询
                     * 是否索引：是，因为需要根据名称字段进行查询
                     * 是否存储：是，因为页面需要展示篇章名字，所以需要存储
                     */
                    if(title.lastIndexOf(title.length()) == '.'){
                        doc.add(new TextField("title", title.substring(0, title.length()-1), Field.Store.YES));
                    }else{
                        doc.add(new TextField("title", title, Field.Store.YES));
                    }
                    docList.add(doc);
                }
            }
        }

        return docList;
    }
    @Test
    public void CreateIndexTest() throws Exception{
        //1 采集数据
        String dataDirectory1 = "D:\\0学习\\数据检索\\爬取三体数据\\小说\\三体1";
        String dataDirectory2 = "D:\\0学习\\数据检索\\爬取三体数据\\小说\\三体2";
        String dataDirectory3 = "D:\\0学习\\数据检索\\爬取三体数据\\小说\\三体3";
        //文档集合
        List<Document> docList1 = getMyDocs(dataDirectory1);
        List<Document> docList2 = getMyDocs(dataDirectory2);
        List<Document> docList3 = getMyDocs(dataDirectory3);

        //3 创建分词器
        Analyzer analyzer = new IKAnalyzer();//标准分词器对英文分词效果好，对中文是单字分词，即，一个字认为是一个词

        //4 创建directory目录对象，目录对象表示索引库的位置
        Directory dir = FSDirectory.open(Paths.get("D:\\ChapterSearchDir2"));//不能new，因为是抽象的。

        //5 创建indexWriterConfig对象，这个对象中指定切分词使用的分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        //6 创建IndexWriter输出流对象，指定输出的位置和使用的config初始化对象
        IndexWriter indexWriter = new IndexWriter(dir, config);//第一个是目录对象，第二个是初始化中文分词器对象

        //7 写入文档到索引库
        for (Document doc : docList1) {
            indexWriter.addDocument(doc);
        }
        for (Document doc : docList2) {
            indexWriter.addDocument(doc);
        }
        for (Document doc : docList3) {
            indexWriter.addDocument(doc);
        }

        //8 释放资源
        indexWriter.close();
    }

    /**
     * 索引库修改操作
     * @throws Exception
     * 原理是把待修改的这一行给删掉，然后从数据库的最后一行插入一个新改出来的
     */
    @Test
    public void UpdateIndexTest() throws Exception{
        //需要变更成的内容
        Document document = new Document();

        document.add(new TextField("contents", "I wrote this content.", Field.Store.YES));
        document.add(new StringField("version","三体2", Field.Store.YES));
        document.add(new TextField("title","其实没有这一章", Field.Store.YES));

        //1 创建分词器
        Analyzer analyzer = new IKAnalyzer();//标准分词器对英文分词效果好，对中文是单字分词，即，一个字认为是一个词

        //4 创建directory目录对象，目录对象表示索引库的位置
        Directory dir = FSDirectory.open(Paths.get("D:\\dir"));//不能new，因为是抽象的。

        //5 创建indexWriterConfig对象，这个对象中指定切分词使用的分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        //6 创建IndexWriter输出流对象，指定输出的位置和使用的config初始化对象
        IndexWriter indexWriter = new IndexWriter(dir, config);//第一个是目录对象，第二个是初始化中文分词器对象

        //修改，第一个参数：修改条件   第二个参数：修改成的内容
        indexWriter.updateDocument(new Term("id", "100000003145"), document);//一般根据业务的主键对象

        //8 释放资源
        indexWriter.close();
    }
    /**
     * 测试根据条件删除
     * @throws Exception
     * 原理是把待修改的这一行给删掉，然后从数据库的最后一行插入一个新改出来的
     */
    @Test
    public void DeleteIndexTest() throws Exception{

        //1 创建分词器
        Analyzer analyzer = new IKAnalyzer();//标准分词器对英文分词效果好，对中文是单字分词，即，一个字认为是一个词

        //4 创建directory目录对象，目录对象表示索引库的位置
        Directory dir = FSDirectory.open(Paths.get("D:\\dir"));//不能new，因为是抽象的。

        //5 创建indexWriterConfig对象，这个对象中指定切分词使用的分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        //6 创建IndexWriter输出流对象，指定输出的位置和使用的config初始化对象
        IndexWriter indexWriter = new IndexWriter(dir, config);//第一个是目录对象，第二个是初始化中文分词器对象

//        //测试根据条件删除
//        indexWriter.deleteDocuments(new Term("id", "100000003145"));

        //测试删除所有内容
        indexWriter.deleteAll();

        //8 释放资源
        indexWriter.close();
    }
}