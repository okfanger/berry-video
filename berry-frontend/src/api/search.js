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


// 搜索记录
