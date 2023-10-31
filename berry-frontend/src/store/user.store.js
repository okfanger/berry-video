import { defineStore } from 'pinia'
import { clearToken, clearUserInfoStorage, getToken, getUserInfoStorage, setUserInfoStorage } from '@/utils'
import { getUserInfo } from '@/api/user'
export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    userInfo: getUserInfoStorage()
  }),
  actions: {
    async fetchUserInfo () {
      try {
        let res = await getUserInfo()
        if (res.status === 200) {
          this.userInfo = res.data
          setUserInfoStorage(this.userInfo)
        }
      } catch (e) {
        console.log(e)
      }
    },
    logout () {
      clearToken()
      clearUserInfoStorage()
    }
  },
  getters: {}

})
