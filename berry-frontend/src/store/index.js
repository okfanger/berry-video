import { useUserStore } from "./user.store"
import { useVideoStore } from "./video.store"

const userStore = useUserStore()
const videoStore = useVideoStore()

export {
  userStore,
  videoStore
}