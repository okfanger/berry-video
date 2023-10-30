

const vScroll = {
  mounted (el) {
    let scrollPosition = 0
    let lock = false // 如果正在滚动中,则不允许再次滚动

    function handleScroll (direction) {
      if (lock) return
      if (direction == 'down' && scrollPosition < el.scrollHeight - el.clientHeight) {
        // Scrolling down
        scrollPosition += el.clientHeight
      } else if (direction == 'up' && scrollPosition > 0) {
        // Scrolling up
        scrollPosition -= el.clientHeight
      }
      el.scrollTo({
        top: scrollPosition,
        behavior: 'smooth'
      })
      lock = true
      setTimeout(() => {
        lock = false
      }, 400)
    }

    // 滚动事件
    function wheelEvent (event) {
      if (event.deltaY > 0) {
        handleScroll('down')
      } else {
        handleScroll('up')
      }
      event.preventDefault()
    }


    // 键盘 ⬆️⬇️键事件
    function keydownEvent (event) {
      if (event.keyCode === 38) {
        handleScroll('up')
      } else if (event.keyCode === 40) {
        handleScroll('down')
      }
    }

    el.addEventListener('wheel', wheelEvent)
    document.addEventListener('keydown', keydownEvent)

    el._whellEvent = handleScroll
    el._keydownEvent = keydownEvent

  },
  unmounted (el) {
    el.removeEventListener('wheel', el._handleScroll)
    document.removeEventListener('keydown', el._keydownEvent)
  }
}
export default vScroll