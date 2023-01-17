<script setup>
import {useRouter} from 'vue-router'
import {ref} from 'vue'

const router = useRouter()
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
function data(){
  return{
    keyword: '从mainpage过来的',
    kind: 'title',
    version: '三体1',
    list:[
      {title: '我嫩爹', content:'八岁的还是你吃吧看不出卡不插卡说不出口'},
      {title: 'hd ', content:'阿布IC不i啊是改变副词u的隔壁u不过i奥斯卡奖杯电脑卡机不插卡就是八块腹肌把款手机拨打科技部擦考教师编反馈阿巴斯空军第八款就不卡不死'},
      {title:'认识', content:'惨哦i是你发错了卡年费拉克丝返回弄i啊'},
      {title:'不', content:'啊哈佛夫i额好吧妇科检查标准库不过覅看'},
      {title:'你好', content:'\t危机纪年第3年，三体舰队距太阳系4.21光年\n怎么看上去这么旧啊面对着唐号正在建造的巨大舰体，吴岳心中首先浮上来的是这样一个念头。其实，他当然知道由于航母舰壳采用最新的汽液保护焊接工艺，会在锰钢板上产生大量并无大碍的污迹。加上闪动的焊弧光产生的效果，才使得即将完和的舰体看上去是他眼前这个样子。他努力让自己想象出唐号涂上灰色船漆后那崭新伟岸的样子，但并不成功。\n　为唐号进行的第四次近海编队训练刚刚完成，在这次为期两个月的航行中，吴岳和站在他身旁的章北海成了两个尴尬的角色。由驱逐舰、潜艇和补给舰组成的编队归战斗群司令官指挥，他们将要指挥的唐号还在建造船坞之中，航空母舰本来要处于的位置由郑和号训练舰填补，有时干脆就空着。这期间吴岳常常在指挥舰上盯着那片空海发呆，那一片水面上，只有前方舰艇留下的航迹在交错中不安地躁动着，恰似他的心绪。这片空白最后真的能填上吗？他不止一次地问自己。\n　　现在再看看建造中的唐号，他看到的已不仅仅是旧了，它甚至有一种古老的沧桑。面前的唐号仿佛是一座被废弃的古代巨型要塞，斑驳的舰体就是要塞高大的石墙，从密密的脚手架上垂下的一缕缕焊花好像是覆盖石墙的植物这不像是建造，倒像是考古吴岳怕自己再想下去，于是把注意力转移到旁边的章北海身上。\n　　父亲的病怎么样了？吴岳问\n　　章北海轻轻摇摇头：不好，也就是维持吧。你请个假吧。他刚住院时我已经请过一次了，现在这形势，到时候再说吧。然后两人就义沉默了，他们之间每一次关于个人生活的交流都是这样，关于工作的谈话肯定会多一些，但也总是隔着一层东西。\n　　北海，以后的工作在分量上可不比以前，既然我们一起到了这个位置上，我想我们之间应该多沟通沟通。吴岳说。\n'}
    ],
    totNum:4,
    currPage: 1
  }
}
function  goChange(title, content) {// 跳转到传递参数页
  router.push({
    path: '/chapter',
    query: {
      T: title,
      C: content
    }// 要传递的参数
  })
}
const datas = data();

function transform(title, content) {
  const sbsbs = {
    title: title,
    content: content
  }
  sessionStorage.setItem('111',JSON.stringify(sbsbs))
  window.open('/chapter', '_blank')
}


if (datas.kind==='title'){
  datas.kind = '题目';
}else{
  datas.kind = '内容';
}
</script>

<template>
  <h2 style="margin-left: 80px;">您按照{{datas.kind}}在{{datas.version}}中对于{{datas.keyword}}的搜索结果，共{{datas.totNum}}条。</h2>
  <div v-for="(item, i) in datas.list" style="margin-left: 80px;">
    <h2>
      <el-link @click="transform(item.title, item.content)" target="_blank" style="font-size: 20px">{{item.title}}</el-link>
    </h2>
    <div style="width:900px">{{item.content.substring(0, 100)}}...</div>
  </div>
  <el-pagination style="margin-left: 1000px;" background layout="prev, pager, next" :total="datas.totNum/8" />
</template>


<style>
.container{
  /* color:aliceblue; */
  text-align: center;
  /* background:url("https://s1.ax1x.com/2022/08/14/vUmrWD.jpg"); */
  /* background:url("D:\0学习\vue学习and实践\supermarket\src\assets\logo.svg"); */
  width:100%;
  height:100%;
  position:fixed;
  background-size:100% 100%;
}

</style>
