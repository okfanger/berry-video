import { defineStore } from 'pinia'
import { getChannelList } from '@/api/video'
export const useVideoStore = defineStore('video', {
  state: () => ({
    channelList: [],
    aa: '',
    bb: ''
  }),
  actions: {

    async fetchChannelList () {
      try {
        let res = await getChannelList()
        const { data, status } = res
        if (status == 200) {
          this.channelList = data
        }
      } catch (e) {
        console.log(e)
      }
    },

  },
  getters: {}

})
