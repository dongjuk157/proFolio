import Vue from 'vue';
import VueRouter from 'vue-router';
import Main from '../views/Main.vue';
import Login from '../views/Login.vue';
import MyPage from '../views/MyPage.vue';
// import Resume from '../views/Resume.vue';
import Them1 from '../views/Them1.vue';
import Them2 from '../views/Them2.vue';
import Them3 from '../views/Them3.vue';
import Convert from '../views/Convert.vue';
import SelectTheme from '../views/SelectTheme.vue';
import ResumeTemp from '../views/ResumeTemp.vue';
import NotFound from '../views/NotFound.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Main',
    component: Main,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/mypage',
    name: 'MyPage',
    component: MyPage,
  },
  // {
  //   path: '/resume',
  //   name: 'Resume',
  //   component: Resume,
  // },
  {
    path: '/resume',
    name: 'Resume',
    component: ResumeTemp,
  },
  {
    path: '/them1',
    name: 'Them1',
    component: Them1,
  },
  {
    path: '/them2',
    name: 'Them2',
    component: Them2,
  },
  {
    path: '/them3',
    name: 'Them3',
    component: Them3,
  },
  {
    path: '/convert',
    name: 'Convert',
    component: Convert,
  },
  {
    path: '/select',
    name: 'SelectTheme',
    component: SelectTheme,
  },
  {
    path: '/notFound',
    name: 'NotFound',
    component: NotFound,
  },
  {
    path: '*',
    redirect: "/notFound",
  },
];

const router = new VueRouter({
  scrollBehavior() {
    return { x: 0, y: 0 };
  },
  mode: 'history',
  routes,
});

export default router;
