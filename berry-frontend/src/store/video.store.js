import { defineStore } from 'pinia'
import { getChannelList, getVideoFeedByTypeApi } from '@/api/video'
import { getChannelListStroge, setChannelListStroge } from '@/utils'

export const useVideoStore = defineStore('video', {
  state: () => ({
    channelList: getChannelListStroge() || [],
  }),
  actions: {
    async fetchChannelList () {
      try {
        let res = await getChannelList()
        const { data, status } = res
        if (status == 200) {
          this.channelList = data
          setChannelListStroge(this.channelList)
        }
      } catch (e) {
        console.log(e)
      }
    },

    async fetchVideoFeedByType (data) {
      return new Promise(resolve => {
        getVideoFeedByTypeApi(data).then(res => {
          const { success, data } = res
          resolve(success ? data.records : [])
        })
      })

    }

  },
  getters: {}

})
