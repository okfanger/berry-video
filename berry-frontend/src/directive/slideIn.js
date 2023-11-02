let observerOptions = {
  threshold: .5,
  root: null, // 如果root为null, 默认为body元素
}
let map = new Map()

// 监听元素与元素之间有没有重叠
const observer = new IntersectionObserver((entries) => {
  entries.forEach(e => {
    if (e.isIntersecting) {
      let index = Array.from(map.keys()).findIndex(item => item === e.target)
      let func = map.get(e.target)
      func(index)
      // console.log(e.target)
    }
  })
}, observerOptions)

const slideIn = {
  mounted (el, binding, vnode) {
    // console.log(vnode)
    observer.observe(el)
    map.set(el, binding.value)
  },
  unmounted (el) {
    observer.unobserve(el)
  },
}

export default slideIn