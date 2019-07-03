<!--登录页面-->
<template>
  <div id="loginBox">
      <Form ref="loginformValidate" :model="loginformValidate" :rules="loginRules">
        <transition name="fade">
          <div class="loginBox">
            <h2>滚石唱片影音授权系统</h2>
            <FormItem  prop="username">
              <Input v-model="loginformValidate.username"  type="text" placeholder="可用手机号、邮箱、用户名登录">
                <Icon type="ios-contact-outline" slot="prepend"/>
              </Input>
            </FormItem>
            <FormItem  prop="password">
              <Input v-model="loginformValidate.password"  type="password" placeholder="密码">
                <Icon type="ios-lock-outline" slot="prepend"></Icon>
              </Input>
            </FormItem>
            <Button type="primary" :loading = "buttonLoading" long @click="loginSubmit('loginformValidate')">{{butText}}</Button>
            <Row>
              <Col :span="8">
                <a href="#" class="register" @click="jumpEmail()">邮箱注册</a>
              </Col>
              <Col :span="8">
                <a href="#" class="register registerA" @click="jumpPhone()">手机注册</a>
              </Col>
              <Col :span="8">
                <a href="#" class="forger">忘记密码?</a>
              </Col>
            </Row>
          </div>
        </transition>
      </Form>
  </div>
</template>

<script>
import store from '../store/index'
export default {
  data() {
    var username = function(rule, value, callback) {
      var phonenum = /^1[3-8][0-9]{9}$/;
      var username = /^[a-zA-Z0-9_]{4,16}$/;
      var email = /^((([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})[; ,])*(([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})))$/;
      if (value === "") {
        callback(new Error("手机号、邮箱、用户名登录"));
      } else if (
        phonenum.test(value) ||
        username.test(value) ||
        email.test(value)
      ) {
        callback();
      } else {
        return callback(new Error("用户名格式不正确"));
      }
    };
    var password = function(rule, value, callback) {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else if (!/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/.test(value)) {
        return callback(
          new Error("密码由6-16字母和数字组成，不能是纯数字或纯英文")
        );
      } else {
        callback();
      }
    };

    var idcard = function(rule, value, callback) {
      var carddl = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|31)|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}([0-9]|x|X)$/;
      var cardtw = /[A-Z][0-9]{9}/;
      var cardam = /^[1|5|7][0-9]{6}\([0-9Aa]\)/;
      var cardxg = /[A-Z]{1,2}[0-9]{6}([0-9A])/;
      if (value === "") {
        callback(new Error("请输入身份证号"));
      } else if (
        carddl.test(value) ||
        cardtw.test(value) ||
        cardam.test(value) ||
        cardxg.test(value)
      ) {
        callback();
      } else {
        return callback(new Error("请输入正确证件号码"));
      }
    };

    return {
      imgsrc:domain.testUrl,
      buttonLoading:false,
      butText:"登录",
      loginformValidate: {
        username: "",
        password: ""
      },

      loginRules: {
        username: [
          {
            validator: username,
            required: true,
            trigger: "blur",
            message: "手机号、邮箱、用户名登录"
          }
        ],
        password: [
          {
            validator: password,
            required: true,
            trigger: "blur",
            min: 6,
            message: "请输入由6-16字母和数字组成的密码"
          }
        ]
      }
    };
  },
  methods: {
    jumpEmail: function() {
      this.$router.push({ path: "/emailRegistered" });
    },
    jumpPhone: function() {
      this.$router.push({ path: "/phoneRegistered" });
    },
    loginSubmit(name) {
      // this.$Spin.show();
      var that = this
      this.buttonLoading = true;
      this.butText = "Loading"
      var param = this.loginformValidate
      this.$refs[name].validate(valid => {
        if (valid) {
          var param = new URLSearchParams()
          param.append('username',this.loginformValidate.username)
          param.append('password',this.loginformValidate.password)
          this.$axios.post(this.imgsrc+'/rock/sso/login.action',param,{
            xhrFields: {
                  withCredentials: true
            }
          })
            .then((res)=> {
              if (res.data.code === 0) {
                that.$Message.info('登录成功！');
                that.$store.commit('SET_TOKEN', res.data.userInfo.userId)
                that.$store.commit('GET_USER', res.data.userInfo.userName)
                that.buttonLoading = false;
                that.butText = "登录"
                that.$router.push("/Home");
              } else if (res.data.code === -1) {
                that.$Message.error('登录失败!'+res.data.msg);
                that.buttonLoading = false;
                that.butText = "登录"
              }
            })
            .catch(function(error) {
              that.buttonLoading = false;
              that.butText = "登录"
              that.$Message.error('提交失败！');
              });
            }
        else{
          // this.$Spin.hide();
          that.$Message.error('请填写账户信息！');
          that.buttonLoading = false;
          that.butText = "登录"
        }
        
      });
    }
  }
};
</script>
<style scoped>
h2 {
  text-align: center;
  color: #fff;
  padding-bottom: 15px;
}
.login {
  height: 100%;
  overflow: hidden;
  background-color: #000;
}
.loginBox {
  width: 30%;
  position: absolute;
  top: 20%;
  left: 35%;
  padding: 20px;
  border-radius: 5px;
  background-color: rgba(255, 255, 255, 0.3);
}

.loginBox button {
  margin-bottom: 15px;
}
.register,
.forger,
.outRegirt {
  display: block;
  color: #fff;
  text-align: left;
}
.forger,
.outRegirt {
  text-align: right;
}
.ivu-input-type .ivu-input-icon {
  left: 0;
}
.ivu-input-type .ivu-input-icon-normal + .ivu-input {
  padding-left: 32px;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.05s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
.ivu-checkbox-wrapper {
  margin-bottom: 10px;
  color: #fff;
}

.registerA {
  text-align: center;
}
</style>
