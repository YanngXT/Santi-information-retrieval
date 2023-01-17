<script setup>
import {useRouter} from 'vue-router'
import {ref} from 'vue'

const router = useRouter()

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
</script>

<template>
  <div style="height:60px"></div>
  <div class="border-div">
    <el-input style="width: 500px;" v-model="input" placeholder="请输入查询关键字" clearable/>
    <div style="height:30px"></div>
    <el-form :model="form" :rules="rules" ref="ruleFormRef" class="my-form">
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
        <el-button type="primary" plain @click="login(ruleFormRef)">搜索</el-button>
      </el-form-item>
    </el-form>
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
</style>
