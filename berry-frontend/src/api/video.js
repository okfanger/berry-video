import { request } from '@/utils'


export const getVideoListByType = (type) => request({
  url: "",
  method: "post",
  data: {
    type
  }
})


export const getVideoFeed = () => request({
  url: "/video/feed",
  method: "get"
})

export const getUpTokenApi = (uuid) => {
  return request({
    url: "/misc/oss/upload/token",
    method: "post",
    data: {
      uuid
    }
  })
}



// 点赞
export const doLikeApi = (videoId) => {
  return request({
    url: "/video/doLike",
    method: "get",
    params: {
      videoId
    }
  })
}

// 取消点赞
export const unLikeApi = (videoId) => {
  return request({
    url: "/video/unLike",
    method: "get",
    params: {
      videoId
    }
  })
}