import { request } from "@/utils"


/**
 * 拿ticket拼接出微信扫码登录的二维码
 * @returns 
 */
export const getQrCode = () => request({
  url: '/misc/wx/ticket',
  method: 'get'
})

export const login = (data) => {
  return request({
    url: `/user/wx/login`,
    method: 'post',
    data
  })
}

export const getUserInfo = () => request({
  url: "/user/info",
  method: "get"
})
