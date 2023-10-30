import { defineStore } from 'pinia'
import { getToken, setToken, clearToken } from '@/utils'
import { getUserInfo } from '@/api/user'
export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken()
  }),
  actions: {
    async login () {

    },
    async fetchUserInfo () {
      try {
        let res = await getUserInfo()
        console.log(res)
      } catch (e) {
        console.log(e)
      }
    },

  },
  getters: {}

})
