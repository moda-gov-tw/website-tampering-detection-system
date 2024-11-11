import {
  createRouter,
  createWebHistory,
  createWebHashHistory,
  RouteRecordRaw,
} from "vue-router";

const Home = () => import("@/component/home.vue");
const Login = () => import("@/account/login.vue");
const TestRouter = () => import("@/component/testRouter.vue");
const TestRouter2 = () => import("@/component/testRouter2.vue");
const searchLog = () => import("@/component/search-log.vue");
const AddServer = () => import("@/component/add-server.vue");
const SearchServer = () => import("@/component/search-server.vue");
const EditServer = () => import("@/component/edit-server.vue");
const SearchServerHome = () => import("@/component/search-serverHome.vue");
const IwgHostList = () => import("@/component/iwg-host-list.vue")
const IwgHostTarget = () => import("@/component/iwg-host-target.vue")
const IwgHostTargetSetting = () => import("@/component/iwg-host-target-setting.vue")

const routes: Array<RouteRecordRaw> = [
  {
    path: "/login",
    name: "login",
    component: Login,
    props: true,
  },
  {
    path: "/",
    name: "home",
    component: Home,
    props: true,
    children: [
      {
        path: "/testRouter",
        name: "testRouter",
        component: TestRouter,
      },
      {
        path: "/testRouter2",
        name: "testRouter2",
        component: TestRouter2,
      },
      {
        path: "/searchLog",
        name: "searchLog",
        component: searchLog,
      },
      {
        path: "/addServer",
        name: "addServer",
        component: AddServer,
      },
      {
        path: "/searchServer",
        name: "searchServer",
        component: SearchServer,
      },
      {
        path: "/editServe/:id",
        name: "editServer",
        component: EditServer,
      },
      {
        path: "/searchServerHome",
        name: "searchServerHome",
        component: SearchServerHome,
      },
      {
        path: "/iwg-host-list",
        name: "IwgHostList",
        component: IwgHostList,
      },
      {
        path: "/iwg-host-target",
        name: "IwgHostTarget",
        component: IwgHostTarget,
      },
      {
        path: "/iwg-host-target/setting/:id",
        name: "IwgHostTargetSetting",
        component: IwgHostTargetSetting,
        props: true,
      },
    ],
  },
  // {
  //   path: "/log",
  //   name: "log",
  //   component: Log,
  // },
  // {
  //   path: "/add",
  //   name: "add",
  //   component: Add,
  // },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes: routes,
});

export default router;
