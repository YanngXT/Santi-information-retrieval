<script setup>
import {useRouter} from 'vue-router'
import {inject, ref} from 'vue'

const router = useRouter()

const axios = inject('axios')

defineExpose({
  name: 'MainPage',
//   props: {
//     UserId: String
//   },
//   data(){
//     return{
//       kind: 'title',
//       version: '1',
//       input: null
//     }
//   }
})

const input = ref('')
const kind = ref('title')
const version = ref('三体1')
const radio = ref(3)

function query() {
  axios.post('/list/sbkeshe', {
    kind: kind.value,
    queryString: input.value,
    version: version.value,
    page: 1
  }).then((response) => {
    console.log(response)
    const data = {
      kind: kind.value,
      queryString: input.value,
      version: version.value,
      page: 1,
      data: response.data
    }
    sessionStorage.setItem('QUERY', JSON.stringify(data))
    router.push('/search')
  }).catch((error) => {
    console.log(error)
  })
  
}


</script>

<template>
  <div style="height:30px"></div>
  <h1 style="text-align:center;color:whitesmoke">欢迎来到三体的世界</h1>
  <div style="height:60px"></div>
  <div class="border-div">
    <el-input style="width: 500px;" v-model="input" placeholder="请输入查询关键字" clearable/>
    <div style="height:70px"></div>
    <el-form :model="form" :rules="rules" ref="ruleFormRef" class="my-form" style="border-color:aliceblue;background: rgba(255,255,255,0.7);">
      <!-- <el-form-item style="text-align:center; height:10px"/> -->
      <el-form-item style="text-align:center">
        <el-container>
          <el-aside width="50px"></el-aside>
          <el-container>
            <el-radio-group v-model="kind" style="text-align:center">
              <el-radio :label="'title'">按题目搜索</el-radio>
              <el-radio :label="'content'">按内容搜索</el-radio>
            </el-radio-group>
          </el-container>
          <el-aside width="30px"></el-aside>
        </el-container>
      </el-form-item>
      <el-form-item  style="text-align:center">
        <el-container>
          <el-aside width="40px"></el-aside>
          <el-container>
            <el-radio-group v-model="version">
              <el-radio :label="'三体1'">三体1</el-radio>
              <el-radio :label="'三体2'">三体2</el-radio>
              <el-radio :label="'三体3'">三体3</el-radio>
            </el-radio-group>
          </el-container>
          <el-aside width="30px"></el-aside>
        </el-container>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" plain @click="query">搜索</el-button>
      </el-form-item>
    </el-form>
    <div style="height:70px"></div>
    <h3 style="text-align:center;color:whitesmoke">给文明以岁月,而不是给岁月以文明。</h3>
  </div>

</template>


<style>
.my-form {
  text-align: center;
  align-items: center;
  border: 1px solid;
  border-radius: 8px;
  padding: 8px;
  margin: auto auto;
  width: 20rem;
  height: 130px;
  display: block;
}

.border-div {
  display: block;
  position: relative;
  top: 20%;
  text-align: center;
  margin: auto;
}

.el-button {
  margin: auto;
}

body{
  background-image: url('../pics/background4.jpg');
  /* background-size:200%; */
  background-position: center;
  height: 100%;
  width: 99%;
}
</style>
