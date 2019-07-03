<!--我的视频授权弹窗-->
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
                    <Input v-model="equipmentValue" @on-change="equipmentValueChange"></Input>
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
                    <label for="">授予权限</label>
                </Col>
                <Col :span="18">
                    <CheckboxGroup v-model="limitsValue" :key="item.value" v-for="item in limitsList" @on-change="limitsValueChange">
                        <Checkbox :label='item.value'>{{item.name}}</Checkbox>
                    </CheckboxGroup>
                </Col>
            </Row>
        </div>
        <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">授予播放时间</label>
                </Col>
                <Col :span="18">
                    <Input v-model="playTimeValue" @on-change="playTimeValueChange"></Input>
                </Col>
            </Row>
        </div>
        <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">授予播放次数</label>
                </Col>
                <Col :span="18">
                    <Input v-model="playNumValue" @on-change="playNumValueChange"></Input>
                </Col>
            </Row>
        </div>
        <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">授予时间段</label>
                </Col>
                <Col :span="8">
                    <Input v-model="starValue" @on-change="starValueChange" placeholder="开始时间单位S"></Input>
                </Col>
                <Col :span="2">
                    <label for="">&nbsp</label>
                </Col>
                <Col :span="8">
                    <Input v-model="stopValue" @on-change="stopValueChange" placeholder="结束时间单位S"></Input>
                </Col>
            </Row>
        </div>
        <div class="wrapper">
            <Row>
                <Col :span="6">
                    <label for="">授予过期日期</label>
                </Col>
                <Col :span="18">
                    <DatePicker type="datetime"  @on-change="overValueChange" format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期" style="width: 100%"></DatePicker>
                </Col>
            </Row>
        </div>
        <div class="wrapper">
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
        </div>
    </Row>
</template>
<script>
export default {
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
      limitsList: [
        {
          name: "可播放",
          value: "play"
        },
        {
          name: "可下载源文件",
          value: "downy"
        },
        {
          name: "可下加密文件",
          value: "dowmj"
        },
        {
          name: "可授权",
          value: "sq"
        }
      ]
    };
  },
  mounted() {
    this.getUsers();
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
    typeValueChange: function() {
      this.$emit("typeValueR", this.typeValue);
    },
    // 获取用户
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
