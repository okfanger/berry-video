import { muted, continuous, isCommoning } from './videoConfig'
import request from './request'
import { getToken, setToken, clearToken } from './token'
import { createUuid } from './createUuid'
import { getUpToken, setUpToken, clearUpToken } from './upToken'
import { getUserInfoStorage, setUserInfoStorage, clearUserInfoStorage } from './userInfo'
import { isLogin } from './validate'
import { parseTime, fromTime } from './parseTime'
import { getChannelListStroge, setChannelListStroge } from './channelList'
import { emitter } from './emitter'

// 统一导出
export {
  // 视频是否支持连续播放
  continuous,
  // 视频是否静音
  muted,
  isCommoning,
  // 二次封装axios
  request,
  // token持久化
  getToken,
  setToken,
  clearToken,
  // uuid
  createUuid,
  // uptoken持久化
  getUpToken,
  setUpToken,
  clearUpToken,
  // userInfo持久化
  getUserInfoStorage,
  setUserInfoStorage,
  clearUserInfoStorage,
  // 判断当前是否登录状态
  isLogin,
  // 日期时间格式转换
  parseTime,
  // 将时间转换为 n天前
  fromTime,
  // 频道持久化
  getChannelListStroge,
  setChannelListStroge,
  // 订阅发布者
  emitter
}
