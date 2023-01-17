<script setup>
import {useRouter} from 'vue-router'
import {inject, ref} from 'vue'

const router = useRouter()
const axios = inject('axios')
/** *
 * 标题1
 * 内容
 *
 * 标题2
 * 内容
 */
defineExpose({
  name: 'SearchResults',
})
//function data(){
//  const tmpData = JSON.parse(sessionStorage.getItem('QUERY'))
//  console.log(tmpData)
//  return{
//    keyword: tmpData.queryString,
//    kind: tmpData.kind,
//    version: tmpData.version,
//    list:tmpData.data.skuList,
//    totNum:tmpData.data.recordCount,
//    currPage: tmpData.data.curPage,
//    pageCount: tmpData.data.pageCount,
//    wordCount: tmpData.data.wordCount,
//  }
//}
function  goChange(title, content) {// 跳转到传递参数页
  router.push({
    path: '/chapter',
    query: {
      T: title,
      C: content
    }// 要传递的参数
  })
}

const tmpData = JSON.parse(sessionStorage.getItem('QUERY'))
let datas = ref({
    keyword: tmpData.queryString,
    kind: tmpData.kind,
    version: tmpData.version,
    list:tmpData.data.skuList,
    totNum:tmpData.data.recordCount,
    currPage: tmpData.data.curPage,
    pageCount: tmpData.data.pageCount,
    wordCount: tmpData.data.wordCount,
    consumeTime: tmpData.data.consumeTime
});

function transform(title, content) {
  const sbsbs = {
    title: title,
    content: content
  }
  sessionStorage.setItem('111',JSON.stringify(sbsbs))
  window.open('/chapter', '_blank')
}

const changePage = (number) => {
  axios.post('/list/sbkeshe', {
    kind: tmpData.kind,
    queryString: tmpData.queryString,
    version: tmpData.version,
    page: number
  }).then((response) => {
    console.log(response.data)
    datas.value = {
      keyword: tmpData.queryString,//response.data.queryString,
      kind: tmpData.kind,//response.data.kind,
      version: tmpData.version,//response.data.version,
      list: response.data.skuList,
      totNum:response.data.recordCount,
      currPage: response.data.curPage,
      pageCount: response.data.pageCount,
      wordCount: response.data.wordCount,
      consumeTime: response.data.consumeTime,
    }
  }).catch((error) => {
    console.log(error)
  })

}

function outputKind(kind){
  if (kind=='title'){
    return '题目';
  }else{
    console.log(kind)
    return '内容';
  }
}


console.log(datas.pageCount)
</script>

<template>
  <h2 style="margin-left: 150px;color:aliceblue">
    您按照
    <span style="color:#aad3e0">{{outputKind(datas.kind)}}</span>
    在{{datas.version}}中
    对于
    "<span style="color:#aad3e0">{{datas.keyword}}</span>"
    所在文章的搜索结果，共<span style="color:#aad3e0">{{datas.totNum}}</span>条。</h2>
  <!-- <h3 style="margin-left: 150px;color:aliceblue">本词在以下章节内共出现了{{datas.wordCount}}次</h3> -->
    <h3 style="margin-left: 150px;color:aliceblue">本词查询用时{{datas.consumeTime}}ms</h3>
  <div v-for="(item, i) in datas.list" style="margin-left: 250px;">
    <el-form :model="form" :rules="rules" ref="ruleFormRef" class="my-form2" style="border-color:aliceblue;background: rgba(255,255,255,0.6);">
    <h3>
      <el-link @click="transform(item.title, item.content)" target="_blank" style="font-size: 20px">{{item.title}}</el-link>
    </h3>
    <div style="width:900px">{{item.content.substring(0, 100)}}...</div>
    </el-form>
    <h3></h3>
  </div>
  
  <el-pagination @current-change="changePage" style="margin-left: 1200px;" background layout="prev, pager, next" :total="datas.pageCount*10" />
  <!-- <el-pagination background layout="prev, pager, next" :total="1000" /> -->
</template>


<style>
.my-form2 {
  /* text-align: center; */
  /* align-items: center; */
  border: 1px solid;
  border-radius: 8px;
  padding: 8px;
  /* margin: auto auto; */
  width: 1000px;
  height: 120px;
  display: block;
}
</style>
