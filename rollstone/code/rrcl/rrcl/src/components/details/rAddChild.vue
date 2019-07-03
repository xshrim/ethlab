<!--授权的视频-授权弹窗-->
<template>
    <Row>
        <div class="wrapper">
            <h4 class="modelTitle">
                <Icon type="university"></Icon>&nbsp;账户授权
            </h4>
        </div>
        <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">授予权限</label>
                </Col>
                <Col :span="18">
                    <CheckboxGroup v-model="limitsValue" @on-change="limitsValueChange">
                        <Checkbox  :key="item.value" v-for="item in limitsList" :label='item.value' :disabled='item.disabled'>{{item.name}}</Checkbox>
                    </CheckboxGroup>
                </Col>
            </Row>
        </div>
        <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">被授予账户</label>
                </Col>
                <Col :span="18">
                    <Select v-model="otherValue" @on-change = "otherValueChange">
                        <Option v-for="item in userList" :value="item.userName" :key="item.userId">{{ item.userName }}</Option>
                    </Select>
                </Col>
            </Row>
        </div>
        <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">设备限制</label>
                </Col>
                <Col :span="18">
                    <Input v-model="equipmentValue" @on-change="equipmentValueChange" readonly></Input>
                </Col>
            </Row>
        </div>
        <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">添加水印</label>
                </Col>
                <Col :span="18">
                    <Input v-model="watermarkValue" @on-change="watermarkValueChange"></Input>
                </Col>
            </Row>
        </div>

        <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">授予播放时间</label>
                </Col>
                <Col :span="18">
                    <Input v-model="playTimeValue" @on-change="playTimeValueChange" :placeholder="this.placeholderTime"></Input>
                </Col>
            </Row>
        </div>
        <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">授予播放次数</label>
                </Col>
                <Col :span="18">
                    <Input v-model="playNumValue" @on-change="playNumValueChange" :placeholder="this.placeholderNum"></Input>
                </Col>
            </Row>
        </div>
        <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">授予时间段</label>
                </Col>
                <Col :span="8">
                    <Input v-model="starValue" @on-change="starValueChange" :placeholder="this.placeholderStart"></Input>
                </Col>
                <Col :span="2">
                    <label for="">&nbsp</label>
                </Col>
                <Col :span="8">
                    <Input v-model="stopValue" @on-change="stopValueChange" :placeholder="this.placeholderStop"></Input>
                </Col>
            </Row>
        </div>
        <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">授予过期日期</label>
                </Col>
                <Col :span="18">
                    <DatePicker type="datetime"  @on-change="overValueChange" format="yyyy-MM-dd HH:mm:ss" :placeholder="this.placeholderOver" style="width: 100%"></DatePicker>
                </Col>
            </Row>
        </div>
        <!-- <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">授予类型</label>
                </Col>
                <Col :span="18">
                    <RadioGroup v-model="typeValue" @on-change="typeValueChange">
                        <Radio label="0">独立授权</Radio>
                        <Radio label="1">委托授权</Radio>
                    </RadioGroup>
                </Col>
            </Row>
        </div> -->
    </Row>
</template>
<script>
export default {
  props: {
    parentParams: {}
  },
  name: "add",
  data() {
    return {
      imgsrc: domain.testUrl,
      otherValue: "",
      equipmentValue: "",
      watermarkValue: "",
      limitsValue: [],
      playTimeValue: "",
      playNumValue: "",
      starValue: "",
      stopValue: "",
      overValue: "",
      typeValue: "0",
      userList: [],
      limitsList: [],
      placeholderTime: "",
      placeholderNum: "",
      placeholderStart: "",
      placeholderStop: "",
      placeholderOver: ""
    };
  },
  mounted() {
    this.getUsers();
    this.getaCcreditData(this.parentParams);
  },
  methods: {
    otherValueChange: function() {
      this.$emit("otherValueR", this.otherValue);
    },
    equipmentValueChange: function() {
      this.$emit("equipmentValueR", this.equipmentValue);
    },
    watermarkValueChange: function() {
      this.$emit("watermarkValueR", this.watermarkValue);
    },
    limitsValueChange: function() {
      this.$emit("limitsValueR", this.limitsValue);
    },
    playTimeValueChange: function() {
      this.$emit("playTimeValueR", this.playTimeValue);
    },
    playNumValueChange: function() {
      this.$emit("playNumValueR", this.playNumValue);
    },
    starValueChange: function() {
      this.$emit("starValueR", this.starValue);
    },
    stopValueChange: function() {
      this.$emit("stopValueR", this.stopValue);
    },
    overValueChange: function(e) {
      this.overValue = e;
      this.$emit("overValueR", this.overValue);
    },
    // typeValueChange: function() {
    //   this.$emit("typeValueR", this.typeValue);
    // },
    getUsers() {
      var that = this;
      this.$axios
        .post(
          this.imgsrc + "/rock/user/getUserList.action",
          {},
          {
            xhrFields: {
              withCredentials: true
            }
          }
        )
        .then(function(res) {
          if (res.data.code === 0) {
            that.userList = res.data.list;
            // this.reload();
          } else if (res.data.code === -1) {
            that.$Message.error("获取数据失败!" + res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error("获取数据失败！" + error);
        });
    },
    //获取授权信息
    getaCcreditData(params) {
      var that = this;
      var param = new URLSearchParams();
      param.append("permId", params.row.permId);
      this.$axios
        .post(this.imgsrc + "/rock/perm/getPermById.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(function(res) {
          if (res.data.code === 0) {
            if(res.data.perm.ptype===0){
              if (res.data.perm.prvs[0] === 1) {
                that.limitsList.push({
                  name: "可播放",
                  value: "play"
                });
              }
              if (res.data.perm.prvs[1] === 1) {
                that.limitsList.push({
                  name: "可下载源文件",
                  value: "downy"
                });
              }
              if (res.data.perm.prvs[2] === 1) {
                that.limitsList.push({
                  name: "可下加密文件",
                  value: "dowmj"
                });
              }
              if (res.data.perm.prvs[3] === 1) {
                that.limitsList.push({
                  name: "可授权",
                  value: "sq"
                });
              }
            }else{
              if (res.data.perm.prvs[0] === 1) {
              that.limitsList.push({
                  name: "可播放",
                  value: "play",
                  disabled:true
                });
              that.limitsValue.push("play")
              }else{
                that.limitsList.push({
                  name: "可播放",
                  value: "play"
                });
              }
              if (res.data.perm.prvs[1] === 1) {
                that.limitsList.push({
                  name: "可下载源文件",
                  value: "downy",
                  disabled:true
                });
                that.limitsValue.push("downy")
              }else{
                that.limitsList.push({
                  name: "可下载源文件",
                  value: "downy"
                });
              }
              if (res.data.perm.prvs[2] === 1) {
                that.limitsList.push({
                  name: "可下加密文件",
                  value: "dowmj",
                  disabled:true
                });
                that.limitsValue.push("dowmj")
              }else{
                that.limitsList.push({
                  name: "可下加密文件",
                  value: "dowmj"
                });
              }
              if (res.data.perm.prvs[3] === 1) {
                that.limitsList.push({
                  name: "可授权",
                  value: "sq",
                  disabled:true
                });
                that.limitsValue.push("sq")
              }else{
                that.limitsList.push({
                  name: "可授权",
                  value: "sq"
                });
              }
            }
            that.equipmentValue = res.data.perm.device;
            that.equipmentValueChange();
            that.placeholderTime =
              res.data.perm.ptime[1] === -1
                ? "无限制"
                : res.data.perm.ptime[1] + "秒";
            that.placeholderNum =
              res.data.perm.ptimes[1] === -1
                ? "无限制"
                : res.data.perm.ptimes[1] + "次";
            that.placeholderStart =
              res.data.perm.pslice[0] === -1
                ? "无限制"
                : res.data.perm.pslice[0] + "s";
            that.placeholderStop =
              res.data.perm.pslice[1] === -1
                ? "无限制"
                : res.data.perm.pslice[1] + "s";
            if (res.data.perm.ptimestamp[1] === -1) {
              that.placeholderOver = "无限制";
            } else {
              that.placeholderOver = new Date(
                res.data.perm.ptimestamp[1] * 1000
              ).toLocaleString();
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
