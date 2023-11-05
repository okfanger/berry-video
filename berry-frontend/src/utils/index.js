import { muted, continuous, isCommoning } from './videoConfig'
import request from './request'
import { getToken, setToken, clearToken } from './token'
import { createUuid } from './createUuid'
import { getUpToken, setUpToken, clearUpToken } from './upToken'
import { getUserInfoStorage, setUserInfoStorage, clearUserInfoStorage } from './userInfo'
import { isLogin } from './validate'
import { parseTime } from './parseTime'
import { getChannelListStroge, setChannelListStroge } from './channelList'
import { emitter } from './emitter'

// 统一导出
export {
  continuous,
  muted,
  isCommoning,
  request,
  getToken,
  setToken,
  clearToken,
  createUuid,
  getUpToken,
  setUpToken,
  clearUpToken,
  getUserInfoStorage,
  setUserInfoStorage,
  clearUserInfoStorage,
  isLogin,
  parseTime,
  getChannelListStroge,
  setChannelListStroge,
  emitter
}
