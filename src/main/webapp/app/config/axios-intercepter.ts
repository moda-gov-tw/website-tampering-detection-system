import axios from "axios";

const TIMEOUT = 1000000;


//在帳號登入成功後會將token偷偷放在session storage裡面
//在透過axios interceptor將存起來的token拿出來,放在header裡面
const onRequestSuccess = (config: any) => {
  console.log('onRequestSuccess');
  const token =
    localStorage.getItem("token") || sessionStorage.getItem("token");
  if (token) {
    if (!config.headers) {
      config.headers = {};
    }
    config.headers.Authorization = `Bearer ${token}`;
  }
  config.timeout = TIMEOUT;
  config.url = `${config.url}`;
  return config;
};

const setupAxiosInterceptors = (onUnauthenticated: any, onServerError: any) => {
  const onResponseError = (err: any) => {
    const status = err.status || err.response.status;
    if (status === 403 || status === 401) {
      return onUnauthenticated(err);
    }
    if (status >= 500) {
      return onServerError(err);
    }
    return Promise.reject(err);
  };

  if (axios.interceptors) {
    axios.interceptors.request.use(onRequestSuccess);
    axios.interceptors.response.use((res) => res, onResponseError);
  }
};
const initAxios = () => {
  //設定 axios  url
  axios.defaults.baseURL = "api";
};

export { onRequestSuccess, setupAxiosInterceptors, initAxios };
