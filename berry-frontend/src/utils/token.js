const tokenKey = 'qiniuToken'

export const getToken = () => {
  let token = localStorage.getItem(tokenKey) || ''
  return token
}

export const setToken = (token) => {
  localStorage.setItem(tokenKey, token)
}

export const clearToken = () => {
  localStorage.removeItem(tokenKey)
}