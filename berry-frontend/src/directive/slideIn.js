import { log } from "vue-emoji-mart"

let observerOptions = {
  threshold: .5,
  root: null, // 如果root为null, 默认为body元素
}


// 监听元素与元素之间有没有重叠
const observer = new IntersectionObserver((entries) => {
  entries.forEach(e => {
    console.log(this.map)
    if (e.isIntersecting) {
      let index = Array.from(this.map.keys()).findIndex(item => item === e.target)
      let func = this.map.get(e.target)
      func(index)
    }
  })
}, observerOptions)

const slideIn = {
  data () {
    return {
      map: new Map()
    }
  },
  mounted (el, binding, vnode) {
    // console.log(vnode)
    observer.observe(el)
    this.map.set(el, binding.value)
  },
  unmounted (el) {
    observer.unobserve(el)
  },
}

export default slideIn