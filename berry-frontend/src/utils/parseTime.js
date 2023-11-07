import moment from 'moment'

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


moment.defineLocale('zh-cn', {
  relativeTime: {
    future: '%s内',
    past: '%s前',
    s: '几秒',
    m: '1分钟',
    mm: '%d分钟',
    h: '1小时',
    hh: '%d小时',
    d: '1天',
    dd: '%d天',
    M: '1个月',
    MM: '%d个月',
    y: '1年',
    yy: '%d年'
  },
})


const fromTime = (timeStr) => {
  const datetime = new Date(timeStr)
  return moment(datetime).fromNow()
}

export {
  parseTime,
  fromTime
}