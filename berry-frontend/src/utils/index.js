import { muted, continuous, isCommoning } from './videoConfig'
import request from './request'
import { getToken, setToken, clearToken } from './token'
import { createUuid } from './createUuid'
import { getUpToken, setUpToken, clearUpToken } from './upToken'

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
  clearUpToken
}