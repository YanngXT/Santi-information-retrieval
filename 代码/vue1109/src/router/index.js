import { createRouter, createWebHistory } from 'vue-router'
import MainPage from "../views/MainPage.vue";
import SearchResults from "../views/SearchResults.vue";
import Chapter from "../views/Chapter.vue";

  const routes =  createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
      {
        path: '/search',
        component: SearchResults
      },
      {
        path: '/',
        component: MainPage
      },
      {
        path: '/chapter',
        component: Chapter
      },
    ]
  })

export default routes
