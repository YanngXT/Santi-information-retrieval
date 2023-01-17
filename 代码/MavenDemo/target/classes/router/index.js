import { createRouter, createWebHistory } from 'vue-router'
import MainPage from "./views/MainPage.html"
import Chapter from "./views/Chapter.html"
import SearchResults from "./views/SearchResults.html"
// import Vue from 'vue'
// import vueRouter from 'vue-router'
// Vue.use(VueRouter)

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/:pathMatch(.*)*',
            name: 'NotFound',
            component: NotFound
        },
        {
            path: '/mainpage',
            name: 'MainPage',
            component: MainPage
        },{
            path: '/chapter',
            name: 'Chapter',
            component: Chapter
        },{
            path: '/searchResults',
            name: 'SearchResults',
            component: SearchResults
        }
    ]
})

export default router
