<!--个人信息页面-->
<template>
    <div class="personInfro">
       <div class="supernatant">
           <div class="changeMessage">
               <a href="#" @click="jumpChangeInformation()" v-show="isShow1">修改资料</a>
               <a href="#" @click="jumpInformation()" v-show="isShow2" :disabled = "isDis">账户认证</a>
               </div>
           <div class="personHeader">
               <Avatar src="http://www.qqzhi.com/uploadpic/2015-01-15/202249542.jpg" size="large"/>
                <div>
                    <h1>{{this.realName}}</h1>
                    <p>{{this.sex}}</p>
                </div>
           </div>
           <div class="personDetails">
               <div>
                    <label for="idcard">
                        身份证号码：<span>{{this.idCard}}</span>
                    </label>
               </div>
               <div>
                    <label for="level">
                        职位级别：<span>{{this.level}}</span>
                    </label>
               </div>
               <div>
                    <label for="phone">
                        手机/电话：<span>{{this.phone}}</span>
                    </label>
               </div>
               <div>
                    <label for="email">
                        邮箱：<span>{{this.email}}</span>
                    </label>
               </div>
               <div>
                    <label for="status">
                        账户认证状态：<span>{{this.status}}</span>
                    </label>
               </div>
               <div v-show="isShow">
                    <label for="changestatus">
                        账户修改认证状态：<span>{{this.changeStatus}}</span>
                    </label>
               </div>
           </div>
       </div>
    </div>
</template>
<script>
export default {
  name: "add",
  data() {
    return {
      imgsrc: domain.testUrl,
      value: "",
      realName: "",
      idCard: "",
      email: "",
      phone: "",
      sex: "",
      level: "",
      status: "",
      addr:"",
      changeStatus: "",
      isShow: false,
      isShow1: true,
      isShow2: false,
      isDis:false,
      resver : ''
    };
  },
  created() {
    // this.userName = this.$store.state.user;
    this.getPersonInformation();
  },
  methods: {
    valueChange: function() {
      var obj = this;
      this.$emit("value", obj.value);
    },
    getPersonInformation: function() {
      var that = this;
      var param = new URLSearchParams();
      param.append("userName", this.userName);

      // this.$axios.post(this.imgsrc+'/rock/user/getUserInfo.action',param,{
      this.$axios
        .post(
          this.imgsrc + "/rock/auth/getAuthUserInfo.action",
          {},
          {
            xhrFields: {
              withCredentials: true
            }
          }
        )
        .then(res => {
          console.log(res)
          this.resver = res
          this.idCard = res.data.userInfo.idcard;
          this.email = res.data.userInfo.email;
          this.phone = res.data.userInfo.phone;
          this.realName = res.data.userInfo.realName;
          if (res.data.changeStatus === 1) {
            this.isShow = true;
            this.changeStatus = "审核中";
          } else if (res.data.changeStatus === 3) {
            this.isShow = true;
            this.changeStatus = "未通过";
          } else if (res.data.changeStatus === -1) {
            this.isShow = false;
          }
          if (res.data.userInfo.sex === 1) {
            this.sex = "男";
          } else {
            this.sex = "女";
          }
          switch (res.data.userInfo.status) {
            case 0:
              this.status = "未认证";
              this.isShow1 = false;
              this.isShow2 = true;
              break;
            case 1:
              this.status = "审核中";
              this.isShow1 = false;
              this.isShow2 = true;
              this.isDis = true;
              break;
            case 2:
              this.status = "通过";
              break;
            case 3:
              this.status = "未通过";
              this.isShow1 = false;
              this.isShow2 = true;
              break;
          }
          switch (res.data.userInfo.level) {
            case 2:
              this.level = "企业管理员";
              break;
            case 5:
              this.level = "企业/公司员工";
              break;
            case 6:
              this.level = "普通认证账户";
              break;
          }
        })
        .catch(function(error) {
          that.$Message.error("获取消息失败！");
        });
    },
    jumpChangeInformation() {
      this.$router.push({ path: "/ChangeInformation" });
    },
    jumpInformation() {
      var that=this
      if(this.resver.data.userInfo.status===0){
        this.$router.push({ path: "/Information" });
      }else{
        switch (that.resver.data.userInfo.level) {
          case 2:
            that.$router.push({ path: "/Level2",query:{params:that.resver} });
            break;
          case 5:
            that.$router.push({ path: "/Level5",query:{params:that.resver} });
            break;
          case 6:
            that.$router.push({ path: "/Level6",query:{params:that.resver} });
            break;
        }
      }
    }
  }
};
</script>
<style scoped>
h4 {
  margin-bottom: 15px;
}
.personHeader {
  width: 50%;
  margin: 0 auto;
  text-align: center;
}
.personDetails {
  width: 50%;
  margin: 0 auto;
  line-height: 40px;
  font-size: 20px;
}
.personDetails > div {
  border-bottom: 1px solid #2d8cf0;
}
.supernatant {
  width: 80%;
  padding: 20px;
  margin: 20px auto;
  border-radius: 5px;
  border: 1px solid #fffed0;
  box-shadow: 0px 0px 12px 1px #2d8cf0;
}
.changeMessage {
  float: right;
}
</style>
