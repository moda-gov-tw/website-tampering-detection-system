import { createApp } from "vue";
import App from "./app.vue";
import router from "./router";
import store from "./store";
import { setupAxiosInterceptors, initAxios } from "@/config/axios-intercepter";
import BootstrapVue3 from 'bootstrap-vue-next'
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue-next/dist/bootstrap-vue-next.css";
import 'bootstrap-icons/font/bootstrap-icons.css'

initAxios();
setupAxiosInterceptors(
  //處理401 error,onUnauthenticatedError
  (error: any) => {
    const url = error.response?.config?.url;
    const status = error.status || error.response.status;
    console.log("Unauthorized!");
    return Promise.reject(error);
  },
  //處理500  error,onServerError 500
  (error: any) => {
    console.log("Server error!");
    return Promise.reject(error);
  }
);
createApp(App).use(BootstrapVue3).use(store).use(router).mount("#app");

