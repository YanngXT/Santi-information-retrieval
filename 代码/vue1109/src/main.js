import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import VueAxios from 'vue-axios'

// import './assets/main.css'

const app = createApp(App)

app.use(router)
app.use(ElementPlus)

const instance = axios.create({
    baseURL: '/apis/',
    timeout: 1000,
  });

app.use(VueAxios, instance)
app.provide('axios', app.config.globalProperties.axios)

app.mount('#app')
