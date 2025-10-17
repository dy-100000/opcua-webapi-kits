import axios from 'axios';

// 创建 axios 实例
const service = axios.create({
  // baseURL: 'http://dingyan3:4840', // 后端服务器地址
  timeout: 15000, // 请求超时时间
  withCredentials: true, // 允许携带凭证
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    return config;
  },
  error => {
    // 对请求错误做些什么
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 对响应数据做点什么
    return response.data;
  },
  error => {
    // 对响应错误做点什么
    console.error('Response error:', error);
    return Promise.reject(error);
  }
);


export const getBrowseNextData = (data) => {
  return service.post('/api/Server-1/browsenext', data);
};

export const getBrowseData = (data) => {
  return service.post('/api/Server-1/browse', data);
};

export const getOpcuaData = (data) => {
  return service.post('/api/Server-1/read', data);
};


export default service;