<!--授权视频列表及一系列操作-->
<template>
    <div>
        <Table border :columns="columns" :data="data"></Table>
        <Page :total="100" show-elevator></Page>
        <Modal
            v-model="modal"
            title="视频详情"
            :footer-hide='true'>
            <p>授予账户：{{this.videoDetails.sgerid}}</p>
            <p>授予日期：{{this.imTime}}</p>
            <p>设备限制：{{this.videoDetails.device}}</p>
            <p>播放时间：{{playtime===-1?"无限制" : playtime+"秒"}}</p>
            <p>播放次数：{{playtimes===-1?"无限制" : playtimes+"秒"}}</p>
            <p>定义时间：{{this.sTime===-1&&this.oTime===-1?"无限制" : this.sTime+"秒"+'--'+this.oTime+"秒"}}</p>
            <p>失效日期：{{playtimes===-1?"无限制" : playtimes}}</p>
            <p>授予权限：{{this.limitsList}}</p>
        </Modal>
    </div>
</template>
<script>
import axios from "axios";
import add from "./rAddChild.vue";
import addd from "./videoWin.vue";
export default {
  inject:["reload"],
  data() {
    return {
      modal: false,
      videoParams: "",
      imgsrc: domain.testUrl,
      tid: "",
      itemId: "",
      otherValueR: "",
      equipmentValueR: "",
      watermarkValueR: "",
      limitsValueR: [],
      playTimeValueR: "",
      playNumValueR: "",
      starValueR: "",
      stopValueR: "",
      overValueR: "",
      videoPathURL: "",
      typeValueR:0,
      videoParam: "",
      videoPath: "",
      videoEndTime: "",
      timeIndex: 0,
      timer: 0,
      playTime: "",
      videoDetails: "",
      limitsList: [],
      playtime: "",
      playtimes: "",
      overTime: "",
      sTime: "",
      oTime: "",
      imTime: "",
      nowTime: "",
      columns: [
        {
          title: "标题",
          key: "title"
        },
        {
          title: "资源上传者",
          key: "uperId"
        },
        {
          title: "资源所有者",
          key: "userId"
        },
        {
          title: "授权时间",
          key: "timestamp"
        },
        {
          title: "资源类型",
          key: "type"
        },
        {
          title: "Action",
          key: "action",
          width: 250,
          align: "center",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.show(params);
                    }
                  }
                },
                "查看"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.openVideo(params);
                    }
                  }
                },
                "播放"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.getDownload(params);
                    }
                  }
                },
                "下载"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.openModal(params);
                    }
                  }
                },
                "授权"
              )
            ]);
          }
        }
      ],
      data: []
    };
  },
  components: {
    addd,
    add
  },
  mounted() {
    this.getData();
  },
  methods: {
    // 详情
    show(params) {
      var that = this;
      this.videoParams = params;
      var param = new URLSearchParams();
      param.append("permId", this.videoParams.row.permId);
      this.$axios
        .post(this.imgsrc + "/rock/perm/getPermById.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(function(res) {
          that.limitsList = [];
          if (res.data.code === 0) {
            if (res.data.perm.prvs[0] === 1) {
              that.limitsList.push("可播放");
            }
            if (res.data.perm.prvs[1] === 1) {
              that.limitsList.push("可下载源文件");
            }
            if (res.data.perm.prvs[2] === 1) {
              that.limitsList.push("可下加密文件");
            }
            if (res.data.perm.prvs[3] === 1) {
              that.limitsList.push("可授权");
            }
            that.modal = true;
            that.videoDetails = res.data.perm;
            that.playtime = res.data.perm.ptime[1];
            that.playtimes = res.data.perm.ptimes[1];
            that.overTime = res.data.perm.ptimestamp[1];
            that.sTime = res.data.perm.pslice[0];
            that.oTime = res.data.perm.pslice[1];
            that.imTime = res.data.perm.ptimestamp[0];
            that.imTime = new Date(that.imTime * 1000).toLocaleString();
          } else if (res.data.code === -1) {
            that.$Message.error(res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error(error);
        });
    },
    // 下载
    getDownload(params) {
      this.videoParams = params;
      let videoUrl = this.videoPath + params.row.path;
      this.downFile(videoUrl);
      this.PlayerPlay("download", "");
    },
    downFile(url) {
      let iframe = document.createElement("iframe");
      iframe.style.display = "none";
      iframe.src = url;
      iframe.onload = function() {
        document.body.removeChild(iframe);
      };
      document.body.appendChild(iframe);
    },
    // 获取列表数据
    getData() {
      var that = this;
      var param = new URLSearchParams();
      param.append("type", 2);
      this.$axios
        .post(this.imgsrc + "/rock/item/getItemList.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(function(res) {
          if (res.data.code === 0) {
            if (res.data.list === null) {
              res.data.list = [];
            } else {
              for (var i in res.data.list) {
                var newTime = new Date(
                  res.data.list[i].permInfo.ptimestamp[0] * 1000
                ).toLocaleString();
                res.data.list[i].timestamp = newTime;
                if (res.data.list[i].type === 1) {
                  res.data.list[i].type = "视频文件";
                } else {
                  res.data.list[i].type = "文本文件";
                }
              }
              that.data = res.data.list;
              that.videoPath = res.data.preFix;
            }
            // this.reload();
          } else if (res.data.code === -1) {
            that.$Message.error("获取数据失败!" + res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error("获取数据失败！" + error);
        });
    },

    // 筛选数组
    IsInArray(arr, val) {
      var testStr = "," + arr.join(",") + ",";
      return testStr.indexOf("," + val + ",") != -1;
    },
    //视频弹窗
    openVideo(params) {
      var that = this;
      this.videoParams = params;
      this.videoPathURL = this.videoPath + this.videoParams.row.path;
      var that = this;
      var param = new URLSearchParams();
      param.append("itemId", this.videoParams.row.permInfo.itemid);
      param.append("permId", this.videoParams.row.permId);
      param.append("action", "play");
      param.append("duration", 0);
      this.$axios
        .post(this.imgsrc + "/rock/log/addLog.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(function(res) {
          if (res.data.code === 0) {
            that.playTime = res.data.perm.ptime[1];
            that.$Modal.confirm({
              scrollable: true,
              render: h => {
                return h(addd, {
                  props: {
                    videoParams: that.videoParams,
                    videoPathURL: that.videoPathURL
                  },
                  on: {
                    videoEndTime: statTime => {
                      that.videoEndTime = statTime;
                    },
                    startTimeMethod: event => {
                      that.startTimer();
                    },
                    stopTimeMethod: event => {
                      clearInterval(that.timer);
                    }
                  }
                });
              },
              onOk: () => {
                clearInterval(that.timer);
                that.PlayerPlay("stop", that.timeIndex);
                clearInterval(that.timer);
                that.timeIndex = 0
              }
            });
          } else if (res.data.code === -1) {
            that.$Message.error(res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error(error);
        });
    },
    //计时器
    startTimer() {
      var that = this;
      this.timer = setInterval(function() {
        that.timeIndex ++;
        if (that.timeIndex > that.playTime) {
          that.PlayerPlay("stop", that.timeIndex);
          that.$Message.error("视频播放时间用尽！");
          that.$Modal.remove();
          clearInterval(that.timer);
          that.reload()
        }
      }, 1000);
    },
    // 授权弹窗
    openModal(params) {
      var that = this;
      this.videoParams = params;
      var param = new URLSearchParams();
      param.append("permId", this.videoParams.row.permId);
      this.$axios
        .post(this.imgsrc + "/rock/perm/getPermById.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(function(res) {
          if (res.data.code === 0) {
            that.nowTime = res.data.sysTimestamp;
            that.overTime = res.data.perm.ptimestamp[1];
            if (
              params.row.permInfo.ptime[1] > 0 ||
              params.row.permInfo.ptime[1] === -1
            ) {
              if (
                params.row.permInfo.ptimes[1] > 0 ||
                params.row.permInfo.ptimes[1] === -1
              ) {
                if (that.nowTime < that.overTime || that.overTime === -1) {
                  that.$Modal.confirm({
                    scrollable: true,
                    okText: "提交",
                    render: h => {
                      return h(add, {
                        props: {
                          parentParams: params
                        },
                        on: {
                          otherValueR: otherValue => {
                            that.otherValueR = otherValue;
                          },
                          equipmentValueR: equipmentValue => {
                            that.equipmentValueR = equipmentValue;
                          },
                          watermarkValueR: watermarkValue => {
                            that.watermarkValueR = watermarkValue;
                          },
                          limitsValueR: limitsValue => {
                            that.limitsValueR = limitsValue;
                          },
                          playTimeValueR: playTimeValue => {
                            that.playTimeValueR = playTimeValue;
                          },
                          playNumValueR: playNumValue => {
                            that.playNumValueR = playNumValue;
                          },
                          starValueR: starValue => {
                            that.starValueR = starValue;
                          },
                          stopValueR: stopValue => {
                            that.stopValueR = stopValue;
                          },
                          overValueR: overValue => {
                            that.overValueR = overValue;
                          },
                          // typeValueR: typeValue => {
                          //   that.typeValueR = typeValue;
                          // }
                        }
                      });
                    },
                    onOk: () => {
                      if (that.playTimeValueR === "" || null) {
                        var newPlayTime = [-1, -1];
                      } else {
                        var newPlayTime = [
                          that.playTimeValueR,
                          that.playTimeValueR
                        ];
                      }
                      if (that.playNumValueR === "" || null) {
                        var newPlayNum = [-1, -1];
                      } else {
                        var newPlayNum = [
                          that.playNumValueR,
                          that.playNumValueR
                        ];
                      }
                      if (that.starValueR === "" || null) {
                        that.starValueR = -1;
                      }
                      if (that.stopValueR === "" || null) {
                        that.stopValueR = -1;
                      }
                      var thisTime = that.overValueR;
                      thisTime = thisTime.replace(/-/g, "/");
                      var newTime = new Date(thisTime);
                      newTime = newTime.getTime();
                      if (that.overValueR === "" || null) {
                        newTime = -1;
                      }
                      var limitsArry = [];
                      if (that.IsInArray(that.limitsValueR, "play")) {
                        limitsArry.push(1);
                      } else {
                        limitsArry.push(0);
                      }
                      if (that.IsInArray(that.limitsValueR, "downy")) {
                        limitsArry.push(1);
                      } else {
                        limitsArry.push(0);
                      }
                      if (that.IsInArray(that.limitsValueR, "dowmj")) {
                        limitsArry.push(1);
                      } else {
                        limitsArry.push(0);
                      }
                      if (that.IsInArray(that.limitsValueR, "sq")) {
                        limitsArry.push(1);
                      } else {
                        limitsArry.push(0);
                      }
                      var param = new URLSearchParams();
                      param.append("userId", that.otherValueR);
                      param.append("tid", params.row.permId);
                      param.append("itemId", params.row.iid);
                      param.append("device", that.equipmentValueR);
                      param.append("pmark", that.watermarkValueR);
                      param.append("prvs", limitsArry);
                      param.append("ptime", newPlayTime);
                      param.append("ptimes", newPlayNum);
                      param.append("psliceStart", that.starValueR);
                      param.append("psliceEnd", that.stopValueR);
                      param.append("ptimestampEnd", newTime);
                      param.append("ptype", that.typeValueR);
                      that.$axios
                        .post(
                          that.imgsrc + "/rock/perm/addPerm.action",
                          param,
                          {
                            xhrFields: {
                              withCredentials: true
                            }
                          }
                        )
                        .then(function(res) {
                          if (res.data.code === 0) {
                            that.$Message.info("提交成功!");
                            // this.reload();
                          } else if (res.data.code === -1) {
                            that.$Message.error("提交失败!" + res.data.msg);
                          }
                        })
                        .catch(function(error) {
                          that.$Message.error("提交失败！" + error);
                        });
                    }
                  });
                } else {
                  that.$Message.error("不在授予日期内");
                }
              } else {
                that.$Message.error("授予播放次数权限用尽");
              }
            } else {
              that.$Message.error("授予时间权限用尽");
            }
          } else if (res.data.code === -1) {
            that.$Message.error(res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error(error);
        });
    },
    //播放视频
    PlayerPlay(action, times) {
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
            that.playTime = res.data.perm.ptime[1];
            // this.reload();
          } else if (res.data.code === -1) {
            // that.$Message.error(res.data.msg);
          }
        })
        .catch(function(error) {
          // that.$Message.error(error);
        });
    }
  }
};
</script>
<style scoped>
.ivu-page {
  text-align: center;
  margin: 15px 0;
}
</style>
