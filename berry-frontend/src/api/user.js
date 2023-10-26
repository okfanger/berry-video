import { request } from "@/utils"


/**
 * 拿ticket拼接出微信扫码登录的二维码
 * @returns 
 */
export const getQrCode = () => request({
  url: '/user/wx/ticket',
  method: 'get'
})

export const getLoginState = (id) => {
  return request({
    url: `/user/wx/ticket/${id}`,
    method: 'get'
  })
}
// 
