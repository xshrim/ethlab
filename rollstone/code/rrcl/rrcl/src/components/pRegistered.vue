<!--手机注册页面-->
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
                <FormItem prop="pass">
                    <Input v-model="ruleForm.pass"  type="password"  placeholder="设置密码">
                    <Icon type="ios-lock-outline" slot="prepend"></Icon>
                    </Input>
                </FormItem>
                <FormItem prop="checkPass">
                    <Input v-model="ruleForm.checkPass" type="password"  placeholder="确认密码">
                    <Icon type="ios-lock-outline" slot="prepend"></Icon>
                    </Input>
                </FormItem>
                <FormItem prop="phonenum">
                    <Input v-model="ruleForm.phonenum" type="text"  placeholder="手机号">
                    <Icon type="md-phone-portrait" slot="prepend"/>
                    </Input>
                </FormItem>
                <FormItem prop="validateCode" class="codeNum">
                    <Row>
                        <Col :span="17">
                            <Input v-model="ruleForm.validateCode" type="text" placeholder="请输入收到的验证码"></Input>
                        </Col>
                        <Button type="primary" v-show="showBtn" class="checkBtn" @click="getCode()">发送验证码</Button>
                        <Button type="primary" v-show="!showBtn" class="checkBtn count">{{count}}</Button>
                    </Row>
                </FormItem>
                <Checkbox v-model="single">同意滚石产品协议</Checkbox>
                <Row :gutter="12">
                    <Col :span="12">
                    <Button type="success" long @click="jump()">返回登录</Button>
                    </Col>
                    <Col :span="12">
                    <Button type="primary" long @click="handleSubmit('ruleForm')">注册</Button>
                    </Col>
                </Row>
                </div>
            </transition>
        </Form> 
    </div>
</template>
<script>
import axios from "../axios.js";
export default {
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
    var pass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.ruleForm.checkPass !== "") {
          this.$refs.ruleForm.validateField("checkPass");
        } else if (
          !/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/.test(value)
        ) {
          return callback(
            new Error("密码由6-16字母和数字组成，不能是纯数字或纯英文")
          );
        } else {
          callback();
        }
      }
    };
    var checkPass = (rule, value, callback) => {
      if (value === "") {
        return callback(new Error("请再次输入密码"));
      } else if (value !== this.ruleForm.pass) {
        return callback(new Error("密码不一致"));
      } else {
        callback();
      }
    };
    // var idcard = (rule, value, callback)=> {
    //   var carddl = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|31)|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}([0-9]|x|X)$/;
    //   var cardtw =/[A-Z][0-9]{9}/;
    //   var cardam =/^[1|5|7][0-9]{6}\([0-9Aa]\)/;
    //   var cardxg =/[A-Z]{1,2}[0-9]{6}([0-9A])/;
    //     if (value === '') {
    //         callback(new Error('请输入身份证号'));
    //     } else if(carddl.test(value)||cardtw.test(value)||cardam.test(value)||cardxg.test(value)){
    //       callback();
    //       } else{
    //         return callback(new Error("请输入正确证件号码"))
    //     }
    // };
    var phonenum = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入手机号"));
      } else if (!/^1[3-8][0-9]{9}$/.test(value)) {
        return callback(new Error("请输入正确手机号码"));
      } else {
        callback();
      }
    };
    var validateCode = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入验证码"));
      } else if (!/^[0-9]*$/.test(value)) {
        return callback(new Error("请输入正确验证码"));
      } else {
        callback();
      }
    };
    return {
      validateName: "获取验证码",
      single: true,
      showBtn: true,
      count: "",
      timer: null,
      ruleForm: {
        pass: "",
        checkPass: "",
        phonenum: "",
        validateCode: "",
        username: ""
      },
      rules: {
        username: [
          {
            validator: username,
            required: true,
            trigger: "blur"
          }
        ],
        pass: [
          {
            validator: pass,
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
        phonenum: [
          {
            validator: phonenum,
            required: true,
            trigger: "blur"
          }
        ],
        validateCode: [
          {
            validator: validateCode,
            required: true,
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    jump: function() {
      this.$router.push({ path: "/userLogin" });
    },
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          axios.userRegister(this.ruleForm).then(({}) => {
            if (data.success) {
              this.$message({
                type: "success",
                message: "注册成功"
              });
            } else {
              this.$message({
                type: "info",
                message: "用户名已经存在"
              });
            }
          });
        }
      });
    },
    // 手机验证码定时器
    getCode: function() {
      const TIME_COUNT = 60;
      if (!this.timer) {
        this.count = TIME_COUNT;
        this.showBtn = false;
        this.timer = setInterval(() => {
          if (this.count > 0 && this.count <= TIME_COUNT) {
            this.count--;
          } else {
            this.showBtn = true;
            clearInterval(this.timer);
            this.timer = null;
          }
        }, 1000);
      }
    }
  }
};
</script>
<style>
.codeNum.ivu-form-item {
  margin-bottom: 0;
}
.codeNum .ivu-form-item-error-tip {
  top: 63%;
}
.checkBtn {
  float: right;
}
</style>
