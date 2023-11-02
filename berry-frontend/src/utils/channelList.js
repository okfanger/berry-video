const channelListKey = "channelList"

export const getChannelListStroge = () => {
  const listStr = localStorage.getItem(channelListKey)
  let result = []
  if (listStr) {
    result = JSON.parse(listStr)
  }
  return result
}


export const setChannelListStroge = (list) => {
  localStorage.setItem(channelListKey, JSON.stringify(list))
}