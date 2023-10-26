import axios from 'axios'
// import { getToken } from './token'
// const url = 'http://geek.itheima.net/v1_0'

const request = axios.create({
  baseURL: "/api",
  timeout: 50000,
})

request.interceptors.request.use(config => {
  // 之后在这里加入token
  // const token = getToken()
  // if (token) {
  //   config.headers.Authorization = `Bearer ${token}`
  // }
  return config
}, err => {
  return Promise.reject(err)
})

request.interceptors.response.use(response => {
  const { data } = response
  return data
}, err => {
  return Promise.reject(err)
})

export default request