<!--我的视频中视频弹窗-->
<template>
  <video-player  class="video-player-box"
                 ref="videoPlayer"
                 :options="playerOptions"
                 :playsinline="true"
                 customEventName="customstatechangedeventname"
                 @play="onPlayerPlay($event)"
                 @pause="onPlayerPause($event)"
                 @ended="onPlayerEnded($event)">
  </video-player>
</template>

<script>

  export default {
    props: {
      videoParams:'',
      videoPathURL:String,
      state: Boolean
    },
    name: "video",
    data() {
      return {
        playerOptions: {
          // videojs options
          muted: true,
          language: 'zh-CN',
          autoplay: true,
          fluid: true,
          playbackRates: [0.7, 1.0, 1.5, 2.0],
          sources: [{
            type: "video/mp4",
            src: this.videoPathURL
          }],
          poster: "/static/images/author.jpg",
        }
      }
    },
    mounted() {
      // console.log('this is current player instance object', this.player)
    },
    computed: {
      player() {
        return this.$refs.videoPlayer.player
      }
    },
    methods: {
      // listen event
      onPlayerPlay(player) {
        // console.log('player play!', player)
      },
      onPlayerPause(player) {
        // console.log('player pause!', player)
      },

      onPlayerEnded(player) {
        // console.log('player ended', playerCurrentState)
      }
    }
  }
</script>