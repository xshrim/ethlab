<!--授权的视频中视频弹窗-->
<template>
  <video-player  class="video-player-box"
                 ref="videoPlayer"
                 :options="playerOptions"
                 :playsinline="true"
                 customEventName="customstatechangedeventname"
                 @play="onPlayerPlay($event)"
                 @pause="onPlayerPause($event)"
                 @ended="onPlayerEnded($event)"
                @playing="onPlayerPlaying($event)" >
  </video-player>
</template>
<script>
import Vue from "vue";
export default {
  props: {
    videoParams:'',
    videoPathURL:String,
    state: Boolean,
    testMethod:''
  },
  name: "addd",
  data() {
    return {
      imgsrc: domain.testUrl,
      // isStart:false,
      statTime:'',
      stopTime:'',
      playerOptions: {
        muted: true,
        language: "zh-CN",
        autoplay: true,
        fluid: true,
        playbackRates: [0.7, 1.0, 1.5, 2.0],
        sources: [
          {
            type: "video/mp4",
            src: this.videoPathURL
          }
        ],
        poster: "/static/images/author.jpg"
      }
    };
  },
  computed: {
    player() {
      return this.$refs.videoPlayer.player;
    }
  },
  created(){
    
  },
  methods: {
    // listen event
    onPlayerPlay(player) {
      this.$emit('startTimeMethod')
    },
    onPlayerPause(player) {
      // this.isStart = true
      // this.statTime = Math.round(player.cache_.currentTime)
      this.$emit('stopTimeMethod')
      // this.$emit('videoEndTime',this.statTime)
    },

    onPlayerEnded(player) {
      // this.isStart = false
      this.stopTime = Math.round(player.cache_.duration)
      this.$emit('stopTimeMethod')
      this.PlayerPlay('stop',this.stopTime);
    },
    onPlayerPlaying (player) {
      setTimeout(()=>{
        this.$emit('videoEndTime',Math.round(player.cache_.currentTime))
      },800)
    },
    PlayerPlay(action,times) {
      var that = this;
      var param = new URLSearchParams();
      param.append("itemId", this.videoParams.row.permInfo.itemid);
      param.append("permId", this.videoParams.row.permId);
      param.append("action", action);
      param.append("duration", times);
      this.$axios
        .post(this.imgsrc + "/rock/log/addLog.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(function(res) {
          if (res.data.code === 0) {

          } else if (res.data.code === -1) {
            that.$Message.error(res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error(error);
        });
    }
  }
};
</script>
<style scoped>
.modelTitle {
  position: absolute;
  top: -26px;
  left: 0;
  color: #2d8cf0;
  display: none;
}
.wrapper {
  margin-top: 15px;
}
.ivu-input {
  color: #2d8cf0;
}
</style>
