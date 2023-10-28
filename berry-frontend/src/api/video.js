import { request } from '@/utils'

export const uploadVideo = (data) => request({
  url: "",
  method: "post",
  data,
})

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
    url: "/video/upload/token",
    method: "post",
    data: {
      uuid
    }
  })
}