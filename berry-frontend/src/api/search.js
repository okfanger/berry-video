import { request } from '@/utils'

// 搜索视频
export const searchVideoApi = ({ keyword, current = 1 }) => request({
  url: "/search/video",
  method: "get",
  params: {
    keyword,
    current
  }
})

// 搜索用户
export const searchUserApi = ({ keyword, current = 1 }) => request({
  url: "/search/user",
  method: "get",
  params: {
    keyword,
    current
  }
})

// 聚合搜索接口 /search/aggregate
export const searchAggregateApi = ({ keyword, current = 1 }) => request({
  url: "/search/aggregate",
  method: "get",
  params: {
    keyword,
    current
  }
})



// 搜索记录
