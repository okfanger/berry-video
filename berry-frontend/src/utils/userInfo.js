const userInfoKey = 'qiniu-userInfo'

export const getUserInfoStorage = () => {
  let UserInfoStr = localStorage.getItem(userInfoKey)
  let result = {}
  if (UserInfoStr) {
    result = JSON.parse(UserInfoStr)
  }
  return result
}

export const setUserInfoStorage = (UserInfo) => {
  localStorage.setItem(userInfoKey, JSON.stringify(UserInfo))
}

export const clearUserInfoStorage = () => {
  localStorage.removeItem(userInfoKey)
}