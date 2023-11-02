function parseTime (timeStr) {
  // 解析日期时间字符串为 Date 对象
  const date = new Date(timeStr)

  // 提取年、月、日、时、分、秒
  const year = date.getFullYear()
  const month = (date.getMonth() + 1).toString().padStart(2, '0') // 月份从0开始，需要加1
  const day = date.getDate().toString().padStart(2, '0')
  const hours = date.getHours().toString().padStart(2, '0')
  const minutes = date.getMinutes().toString().padStart(2, '0')
  const seconds = date.getSeconds().toString().padStart(2, '0')

  // 构建年月日时分秒格式
  const formattedDateTime = `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`
  return formattedDateTime
}

export {
  parseTime
}