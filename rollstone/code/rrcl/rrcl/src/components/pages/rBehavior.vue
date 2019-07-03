<!--行为追踪-->
<template>
    <div>
        <Row>
            <Col span="8">
                <DatePicker type="date" placeholder="Select date" :open="true" :value="value" style="width: 216px;margin-left: 22%;" @on-change="changeValue"></DatePicker>
            </Col>
            <Col span="16">
                <timeline timeline-theme="lightblue">
                    <timeline-title>{{this.$store.state.user}}</timeline-title>
                    <timeline-item bg-color="#e6b6b0" :hollow="true" v-for="item in this.actionList" :key="item.mid">
                        <h4>行为：{{item.operate}}</h4>
                        <p>时间：{{new Date(parseInt(item.timestamp) * 1000).toLocaleString()}}</p>
                        <p>操作的节点：{{item.details.node}}</p>
                    </timeline-item>
                    <timeline-item bg-color="#e6b6b0" :hollow="true" v-show="isShow">
                        <h4>当日无操作</h4>
                    </timeline-item>
                </timeline>
            </Col>
        </Row>
    </div>
</template>

<script>
import { Timeline, TimelineItem, TimelineTitle } from "vue-cute-timeline";
export default {
  data() {
    return {
      value: "",
      isShow: false,
      imgsrc: domain.testUrl,
      user: "",
      actionList: []
    };
  },
  components: {
    Timeline,
    TimelineItem,
    TimelineTitle
  },
  created() {
    //获取当前时间
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    if (month < 10) {
      month = "0" + month;
    }
    if (day < 10) {
      day = "0" + day;
    }
    var nowDate = year + "-" + month + "-" + day;
    nowDate = nowDate.replace(/-/g, "/");
    var newTime = new Date(nowDate);
    newTime = newTime.getTime() / 1000;
    this.getLogos(newTime);
  },
  methods: {
    changeValue(e) {
      this.value = e;
      var thisTime = this.value;
      thisTime = thisTime.replace(/-/g, "/");
      var newTime = new Date(thisTime);
      newTime = newTime.getTime() / 1000;
      this.getLogos(newTime);
    },
    getLogos(timestamp) {
      var that = this;
      var param = new URLSearchParams();
      param.append("timestamp", timestamp);
      this.$axios
        .post(this.imgsrc + "/rock/log/getLogs.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(function(res) {
          if (res.data.code === 0) {
            that.actionList = [];
            if (res.data.list.length === 0) {
              that.isShow = true;
            } else {
              that.isShow = false;
            }
            for (var i in res.data.list) {
              switch (res.data.list[i].operate) {
                case "addUser":
                  res.data.list[i].operate = "添加用户";
                  break;
                case "uplItem":
                  res.data.list[i].operate = "上传资源";
                  break;
                case "addPerm":
                  res.data.list[i].operate = "资源授权";
                  break;
                case "play":
                  res.data.list[i].operate = "播放视频";
                  break;
                case "stop":
                  res.data.list[i].operate = "停止视频用户";
                  break;
              }
              that.actionList = res.data.list;
            }
          } else if (res.data.code === -1) {
            that.$Message.error("获取数据失败!" + res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error("获取数据失败！" + error);
        });
    }
  }
};
</script>

<style>
.append {
  font-size: 0.8em;
  margin-top: 3px;
  color: #646c7c;
}
a {
  color: #bf6dcf;
  font-weight: bold;
  text-decoration: none;
}
.icon-heart {
  width: 20px;
}
.github-corner:hover .octo-arm {
  animation: octocat-wave 560ms ease-in-out;
}
@keyframes octocat-wave {
  0%,
  100% {
    transform: rotate(0);
  }
  20%,
  60% {
    transform: rotate(-25deg);
  }
  40%,
  80% {
    transform: rotate(10deg);
  }
}
@media (max-width: 500px) {
  .github-corner:hover .octo-arm {
    animation: none;
  }
  .github-corner .octo-arm {
    animation: octocat-wave 560ms ease-in-out;
  }
}
.timeline-others {
  background-color: inherit;
}
</style>