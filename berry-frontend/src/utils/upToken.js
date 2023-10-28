const UPTOKEN = 'qiniu-up-token'

export const getUpToken = () => {
  let upToken = localStorage.getItem(UPTOKEN) || ''
  return upToken
}

export const setUpToken = (upToken) => {
  localStorage.setItem(UPTOKEN, upToken)
}

export const clearUpToken = () => {
  localStorage.removeItem(UPTOKEN)
}