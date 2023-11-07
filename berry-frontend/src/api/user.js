import { request } from "@/utils"


/**
 * 拿ticket拼接出微信扫码登录的二维码
 * @returns 
 */
export const getQrCode = () => request({
  url: '/misc/wx/ticket',
  method: 'get'
})

// 登录接口
export const login = (data) => {
  return request({
    url: `/user/wx/login`,
    method: 'post',
    data
  })
}


// 获取登录用户信息
export const getUserInfo = () => request({
  url: "/user/info",
  method: "get"
})

export const getUserInfoVo = (authorId) => request({
  url: "/user/info/vo",
  method: "get",
  params: {
    authorId
  }
})


// 更换用户头像
export const updateAvatar = (formData) => {
  return request({
    url: "/misc/oss/upload/avatar",
    method: 'post',
    data: formData
  })
}


// 关注
export const followUserApi = (userId) => {
  return request({
    url: '/action/follow/doFollow',
    method: "get",
    params: {
      userId
    }
  })
}

// 取消关注
export const unfollowUserApi = (userId) => {
  return request({
    url: '/action/follow/unFollow',
    method: "get",
    params: {
      userId
    }
  })
}