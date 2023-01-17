<script>
import {useRouter} from 'vue-router'

const router = useRouter()
  export default({
    name: 'Chapter',
    beforeMount() {
      const data = JSON.parse(sessionStorage.getItem('111'))
      this.title = data.title
      this.content = data.content
    },
    data() {
      return {
        title: null,
        content: null
      }
    }
  })
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header style="text-align:center"><h1>{{title}}</h1></el-header>
      <el-container>
        <el-aside width="300px"></el-aside>
        <el-container>
          <el-main style="white-space: pre-wrap; position:relative;">{{content}}</el-main>
        </el-container>
        <el-aside width="300px"></el-aside>
      </el-container>
    </el-container>
  </div>
</template>


<style>
.container{
  /* color:aliceblue; */
  text-align: center;
  /* background:url("https://s1.ax1x.com/2022/08/14/vUmrWD.jpg"); */
  /* background:url("D:\0学习\vue学习and实践\supermarket\src\assets\logo.svg"); */
  width:60%;
  height:100%;
  /* position:fixed; */
  background-size:100% 100%;
}
.container2{
  /* color:aliceblue; */
  /* background:url("https://s1.ax1x.com/2022/08/14/vUmrWD.jpg"); */
  /* background:url("D:\0学习\vue学习and实践\supermarket\src\assets\logo.svg"); */
  width:60%;
  height:70%;
  /* position:fixed; */
  background-size:100% 100%;
}
</style>
