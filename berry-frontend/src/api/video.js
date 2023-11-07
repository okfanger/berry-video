import { request } from '@/utils'

// 获取推荐视频流 || 频道视频
export const getVideoFeed = (channelId) => {
  if (channelId || channelId === 0)
    return request({
      url: "/video/feed",
      method: "get",
      params: {
        channelId
      }
    })
  else return request({
    url: "/video/feed",
    method: "get"
  })
}

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
    url: "/action/video/doLike",
    method: "get",
    params: {
      videoId
    }
  })
}

// 取消点赞
export const unLikeApi = (videoId) => {
  return request({
    url: "/action/video/unLike",
    method: "get",
    params: {
      videoId
    }
  })
}

// 收藏
export const doCollectApi = (videoId) => {
  return request({
    url: "/action/video/doFavor",
    method: "get",
    params: {
      videoId
    }
  })
}

// 取消收藏
export const unCollectApi = (videoId) => {
  return request({
    url: "/action/video/unFavor",
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


// 发布评论
export const publisComment = (videoId, content) => {
  return request({
    url: '/action/video/comment',
    method: "post",
    data: {
      videoId,
      content
    }
  })
}

// 给评论点赞
export const doLikeForCommentApi = (commentId) => {
  return request({
    url: "/action/video/comment/doLike",
    method: 'get',
    params: {
      commentId
    }
  })
}

// 给评论取消点赞
export const unLikeForCommentApi = (commentId) => {
  return request({
    url: "/action/video/comment/unLike",
    method: 'get',
    params: {
      commentId
    }
  })
}

// 获取视频的所有评论列表 分页 需要懒加载
export const getVideoCommentList = ({ videoId, current = 1, orderBy = 'time' }) => {
  return request({
    url: "/action/video/comment/feed",
    method: "get",
    params: {
      videoId,
      current,
      orderBy //按照什么排序
    }
  })
}


// 根据类型获取feed流 like favor product
export const getVideoFeedByTypeApi = ({ type, authorId }) => {
  let baseUrl = '/video/my'
  const suffix = type == 'product' ? '' : `/${type}`
  return request({
    url: baseUrl + suffix,
    method: "get",
    params: {
      authorId
    }
  })
}

// 当前用户发布的视频
export const getMyselfVideoApi = (current = 1) => {
  return request({
    url: "/video/my",
    method: "get",
    params: {
      current
    }
  })
}

// 删除已经发布的视频
export const deleteVideoApi = (videoId) => {
  return request({
    url: '/video/recycle',
    method: "post",
    params: {
      videoId
    }
  })
}


// 删除评论



