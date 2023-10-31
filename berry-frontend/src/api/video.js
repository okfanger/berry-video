import { request } from '@/utils'

export const getVideoFeed = () => request({
  url: "/video/feed",
  method: "get"
})

// 获取上传token
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

// 收藏
export const doCollectApi = (videoId) => {
  return request({
    url: "/video/doFavor",
    method: "get",
    params: {
      videoId
    }
  })
}

// 取消收藏
export const unCollectApi = (videoId) => {
  return request({
    url: "/video/unFavor",
    method: "get",
    params: {
      videoId
    }
  })
}

// 频道分类列表 List
export const getChannelList = (channelId) => request({
  url: "/video/channel/list",
  method: "get",
  params: {
    channelId
  }
})

// 发布视频
export const publishVideo = (data) => request({
  url: "/video/publish",
  method: "post",
  data
})

// 根据channelId 获取视频feed
export const getChannelFeedByChannelIdApi = (channelId) => {
  return request({
    url: "/video/channelFeed",
    method: "get",
    params: {
      channelId
    }
  })
}


// 发布评论
export const publisComment = (videoId, content) => {
  return request({
    url: '/video/comment',
    method: "post",
    data: {
      videoId,
      content
    }
  })
}

// 给评论点赞
export const doLikeForCommentApi = () => {

}

// 获取视频的所有评论列表 分页 需要懒加载
export const getVideoCommentList = ({ videoId, current = 1 }) => {
  return request({
    url: "/video/comment/feed",
    method: "get",
    params: {
      videoId,
      current,
      // orderBy //按照什么排序
    }
  })
}