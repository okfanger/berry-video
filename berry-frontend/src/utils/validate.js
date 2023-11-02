import { getToken } from './token'

export function isLogin () {
  return getToken() !== '' ? true : false
}