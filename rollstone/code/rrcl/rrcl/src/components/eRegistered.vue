<!--邮箱注册页面-->
<template>
    <div>
        <Form ref="ruleForm" :model="ruleForm" :rules="rules">
            <transition name="fade">
                <div class="loginBox registerBox" >
                <h2>滚石唱片影音授权系统</h2>
                <FormItem prop="username">
                    <Input v-model="ruleForm.username" type="text" placeholder="用户名">
                    <Icon type="ios-contact" slot="prepend"/>
                    </Input>
                </FormItem>
                <FormItem prop="pwd">
                    <Input v-model="ruleForm.pwd"  type="password"  placeholder="设置密码">
                    <Icon type="ios-lock-outline" slot="prepend"></Icon>
                    </Input>
                </FormItem>
                <FormItem prop="checkPass">
                    <Input v-model="ruleForm.checkPass" type="password"  placeholder="确认密码">
                    <Icon type="ios-lock-outline" slot="prepend"></Icon>
                    </Input>
                </FormItem>
                <FormItem prop="email" class="vaildCode">
                    <Row>
                        <Col :span="17">
                        <Input v-model="ruleForm.email" type="text" placeholder="E-mail">
                          <Icon type="md-mail" slot="prepend"/>
                        </Input>
                        </Col>
                        <Col :span="6" :offset="1">
                          <Button type="primary" :loading = "buttonLoading" class="checkBtn" @click="getCode()">{{butText}}</Button>
                        </Col>
                    </Row>
                </FormItem>
                <FormItem prop="vaildCode">
                    <Input v-model="ruleForm.vaildCode" type="text" placeholder="验证码"></Input>
                </FormItem>
                <Checkbox v-model="single">同意滚石产品协议</Checkbox>
                <Row :gutter="12">
                    <Col :span="12">
                        <Button type="success" long @click="jump()">返回登录</Button>
                    </Col>
                    <Col :span="12">
                        <Button type="primary" long :loading = "RbuttonLoading" @click="handleSubmit('ruleForm')">{{registerText}}</Button>
                    </Col>
                </Row>
                </div>
            </transition>
        </Form> 
    </div>
</template>
<script>
// import axios from "../axios.js";
export default {
  inject: ["reload"],
  data() {
    var username = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入用户名"));
      } else if (!/^[a-zA-Z0-9_]{4,16}$/.test(value)) {
        return callback(new Error("用户名由4到16位（字母，数字，下划线）组成"));
      } else {
        callback();
      }
    };
    var pwd = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else if (!/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/.test(value)) {
        return callback(
          new Error("密码由6-16字母和数字组成，不能是纯数字或纯英文")
        );
      } else {
        if (this.ruleForm.checkPass !== "") {
          // 对第二个密码框单独验证
          this.$refs.ruleForm.validateField("checkPass");
        }
        callback();
      }
    };
    var checkPass = (rule, value, callback) => {
      if (value === "") {
        return callback(new Error("请再次输入密码"));
      } else if (value !== this.ruleForm.pwd) {
        return callback(new Error("密码不一致"));
      } else {
        callback();
      }
    };
    var email = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入邮箱"));
      } else if (
        !/^((([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})[; ,])*(([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})))$/.test(
          value
        )
      ) {
        return callback(new Error("请输入邮箱"));
      } else {
        callback();
      }
    };
    return {
      imgsrc: domain.testUrl,
      buttonLoading: false,
      RbuttonLoading: false,
      single: true,
      butText: "获取验证码",
      registerText: "注册",
      ruleForm: {
        pwd: "",
        checkPass: "",
        email: "",
        username: "",
        vaildCode: ""
      },
      rules: {
        username: [
          {
            validator: username,
            required: true,
            trigger: "blur"
          }
        ],
        pwd: [
          {
            validator: pwd,
            required: true,
            trigger: "blur",
            min: 6
          }
        ],
        checkPass: [
          {
            validator: checkPass,
            required: true,
            trigger: "blur"
          }
        ],
        email: [
          {
            validator: email,
            required: true,
            trigger: "blur"
          }
        ],
        vaildCode: [
          {
            required: true,
            trigger: "blur",
            message: "请输入验证码"
          }
        ]
      }
    };
  },
  methods: {
    jump: function() {
      this.$router.push({ path: "/userLogin" });
    },
    // 获取验证码
    getCode: function() {
      var that = this;
      this.buttonLoading = true;
      this.butText = "Loading";
      let param = new URLSearchParams();
      param.append("email", this.ruleForm.email);
      this.$axios
        .post(this.imgsrc + "/rock/user/checkEmail.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(res => {
          if (res.data.code === 0) {
            that.$Message.info("发送成功！");
            that.buttonLoading = false;
            that.butText = "获取验证码";
          } else if (res.data.code === -1) {
            that.$Message.error("发送失败！");
            that.buttonLoading = false;
            that.butText = "获取验证码";
          }
        });
    },
    handleSubmit(name) {
      var that = this;
      this.RbuttonLoading = true;
      this.registerText = "Loading";
      this.$refs[name].validate(valid => {
        if (valid) {
          var param = new URLSearchParams();
          param.append("userName", this.ruleForm.username);
          param.append("pwd", this.ruleForm.pwd);
          param.append("email", this.ruleForm.email);
          param.append("vaildCode", this.ruleForm.vaildCode);
          this.$axios
            .post(this.imgsrc + "/rock/user/register.action", param, {
              xhrFields: {
                withCredentials: true
              }
            })
            .then(res => {
              if (res.data.code === 0) {
                that.$Message.info("注册成功！");
                that.$router.push({ path: "/userLogin" });
                that.RbuttonLoading = false;
                that.reload();
                that.registerText = "注册";
              } else if (res.data.code === -1) {
                that.$Message.error("注册失败!" + res.data.msg);
                that.RbuttonLoading = false;
                that.registerText = "注册";
              }
            })
            .catch(function(error) {
              that.$Message.error("提交失败！");
              that.RbuttonLoading = false;
              that.registerText = "注册";
            });
        } else {
          this.$Message.error("填写数据错误!");
          this.RbuttonLoading = false;
          this.registerText = "注册";
        }
      });
    }
  }
};
</script>
<style>
.vaildCode.ivu-form-item {
  margin-bottom: 0;
}
.vaildCode .ivu-form-item-error-tip {
  top: 63%;
}
.checkBtn {
  float: right;
}
</style>
