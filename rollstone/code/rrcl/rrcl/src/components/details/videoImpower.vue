<!--授权的视频中视频弹窗-->
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
  name:"addd",
  data() {
    return {
      videoPathUrl: "",
      imgsrc: domain.testUrl,
      playerOptions: {
        // videojs options
        muted: true,
        language: "zh-CN",
        autoplay: false,
        fluid: true,
        playbackRates: [0.7, 1.0, 1.5, 2.0],
        sources: [
          {
            type: "video/mp4",
            src:this.videoPathUrl
          }
        ],
        poster: "/static/images/author.jpg"
      }
    };
  },
  watch:{
    videoPathUrl:function(val){
      this.$refs.videoPlayer.player.src(val)
    },
    state: function(val){
        if(val){
          this.$refs.videoPlayer.player.pause()
        }
    }
  },
  mounted() {
    },
  created() {
  },
  computed: {
    player() {
      return this.$refs.videoPlayer.player;
    }
  },
  methods: {
    // listen event
    onPlayerPlay(player) {
      // console.log('player play!', player)
      this.PlayerPlay();
    },
    onPlayerPause(player) {
      // console.log('player pause!', player)
    },

    onPlayerEnded(player) {
      // console.log('player ended', playerCurrentState)
    },
    PlayerPlay() {
      var that = this;
      var param = new URLSearchParams();
      param.append("itemId", this.videoParams.row.permInfo.itemid);
      param.append("permId", this.videoParams.row.permId);
      param.append("action", "play");
      param.append("duration", 5);
      this.$axios
        .post(this.imgsrc + "/rock/log/addLog.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(function(res) {
          if (res.data.code === 0) {
            // this.reload();
          } else if (res.data.code === -1) {
            that.$Message.error("获取数据失败!" + res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error("获取数据失败！" + error);
        });
    }
  }
}
</script>