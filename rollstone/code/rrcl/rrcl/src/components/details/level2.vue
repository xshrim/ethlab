<!--重新认证信息-管理员-->
<template>
    <div class="information">
        <div class="infor">
            <h3>请您完善信息！提交后审核通过即可获得更多权限</h3>
              <Form ref="formValidate" :model="formValidate" label-position="left" :label-width="100" :rules="ruleValidate">
                  <FormItem label="姓名" prop="realname">
                      <Input v-model="formValidate.realname"></Input>
                  </FormItem>
                  <FormItem label="身份证号" prop="idcard">
                      <Input v-model="formValidate.idcard"></Input>
                  </FormItem>
                  <FormItem label=" " >
                      <div>
                          <Upload
                              ref="file"
                              :with-credentials= true
                              :on-success="uploadSuccessId"
                              :on-error="uploadError"
                              name="file"
                              :action="action1"
                              >
                              <Button icon="ios-cloud-upload-outline">上传身份证照片</Button>
                          </Upload>
                      </div>
                  </FormItem>
                  <FormItem prop="birthdate" label="出生日期">
                      <DatePicker type="date" placeholder="选择日期" :value="formValidate.birthdate" @on-change="birthdateValue"></DatePicker>
                  </FormItem>
                  <FormItem label="性别" prop="sex">
                      <RadioGroup v-model="formValidate.sex">
                          <Radio :label="1">男</Radio>
                          <Radio :label="2">女</Radio>
                      </RadioGroup>
                  </FormItem>
                  <!-- <FormItem label="年龄" prop="age">
                      <Input v-model="formValidate.age"></Input>
                  </FormItem> -->
                  <FormItem label="公司名称" prop="company">
                      <Input v-model="formValidate.company"></Input>
                  </FormItem>

                  <FormItem label="">
                      <div>
                          <Upload
                              ref="file"
                              :with-credentials= true
                              :on-success="uploadSuccess"
                              :on-error="uploadError"
                              name="file"
                              :action="action2"
                              >
                              <Button icon="ios-cloud-upload-outline">上传公司资质文件</Button>
                          </Upload>
                      </div>
                  </FormItem>
                  <FormItem label="选择已有公司" prop="companyId">
                      <Select v-model="formValidate.companyId" placeholder="请选择您所属的公司">
                          <Option  v-for="item in companyList" :key="item.id" :value="item.id">{{item.name}}</Option>
                      </Select>
                  </FormItem>
                  <FormItem label="联系地址" prop="Address">
                      <Input v-model="formValidate.Address"></Input>
                  </FormItem>
                  <FormItem label="联系电话" prop="call">
                      <Input v-model="formValidate.call"></Input>
                  </FormItem>
                  <FormItem label="Email" prop="email">
                      <Input v-model="formValidate.email"></Input>
                  </FormItem>
              </Form>
              <div class="btns">
                  <Button type="success" @click="handleReset('formValidate')">重置</Button>
                  <Button type="primary" @click="handleSubmit('formValidate')" :disabled='btnDis' :loading = "buttonLoading">提交</Button>
              </div>
        </div>
    </div>
</template>
<script>
export default {
  inject: ["reload"],
  data() {
    return {
      imgsrc: domain.testUrl,
      credentials: true,
      userId: "",
      cardImg: "",
      companyImg: "",
      btnDis: false,
      buttonLoading: false,
      companyList: "",
      action1: domain.testUrl + "/rock/file/upload.action",
      action2: domain.testUrl + "/rock/file/upload.action",
      formValidate: {
        realname: "",
        Address: "",
        sex: "",
        idcard: "",
        birthdate: "",
        age: "",
        company: "",
        qualification: "",
        call: "",
        email: "",
        level: "2",
        companyId: ""
      },
      ruleValidate: {
        realname: [
          { required: true, message: "用户名不能为空", trigger: "blur" }
        ],
        idcard: [
          { required: true, message: "身份证号不能为空", trigger: "blur" }
        ],
        birthdate: [
          {
            required: true,
            type: "string",
            message: "出生日期不能为空",
            trigger: "change"
          }
        ],
        age: [{ required: true, message: "年龄不能为空", trigger: "blur" }],
        sex: [{ required: true, message: "请选择性别", trigger: "blur" }],
        company: [
          { required: true, message: "公司名称不能为空", trigger: "blur" }
        ],
        Address: [
          { required: true, message: "联系地址不能为空", trigger: "blur" }
        ],
        call: [
          { required: true, message: "联系电话不能为空", trigger: "blur" }
        ],
        email: [{ required: true, message: "邮箱不能为空", trigger: "blur" }]
      }
    };
  },
  components: {
  },
  created() {
    this.userId = this.$store.state.token;
    this.userMessge = this.$route.query.params
    this.getCompany();
    this.getUserMesssge()
  },
  methods: {
    //日期格式转换
    birthdateValue(e) {
      this.formValidate.birthdate = e;
    },
    // 提交数据
    handleSubmit(name) {
      var that = this;
      this.$refs[name].validate(valid => {
        if (valid) {
          that.buttonLoading = true;
          var param = new URLSearchParams();
          //非认证
          param.append("userId", that.userId);
          param.append("email", that.formValidate.email);
          param.append("realName", that.formValidate.realname);
          param.append("idcard", that.formValidate.idcard);
          param.append("phone", that.formValidate.call);
          param.append("idcardPhoto", this.cardImg);
          //认证
          param.append("addr", that.formValidate.Address);
          param.append("birthdayStr", that.formValidate.birthdate);
          param.append("sex", that.formValidate.sex);
          param.append("level", that.formValidate.level);
          param.append("companyId", that.formValidate.companyId);
          param.append("companyInfo.name", that.formValidate.company);
          param.append("companyInfo.apt", that.companyImg);
          that.$axios
            .post(that.imgsrc + "/rock/auth/submitInfo.action", param, {
              xhrFields: {
                withCredentials: true
              }
            })
            .then(function(res) {
              if (res.data.code === 0) {
                that.btnDis = true;
                that.$Message.info(
                  "提交成功!请耐心等待审核，审核时间为1~3天！"
                );
                this.reload();
                that.buttonLoading = false;
                that.$router.push("/Home");
              } else if (res.data.code === -1) {
                that.$Message.error("提交失败!" + res.data.msg);
                that.buttonLoading = false;
              }
            })
            .catch(function(error) {
              that.$Message.error("提交失败！" + error);
              that.buttonLoading = false;
            });
        } else {
          this.$Message.error("请填写完整信息!");
        }
      });
    },
    // 获取公司列表
    getCompany() {
      var that = this;
      this.$axios
        .get(
          this.imgsrc + "/rock/company/getComapnyList.action",
          {},
          {
            xhrFields: {
              withCredentials: true
            }
          }
        )
        .then(function(res) {
          if (res.data.code === 0) {
            that.companyList = res.data.list;
          } else if (res.data.code === -1) {
            that.$Message.error("提交失败!" + res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error("提交失败！" + error);
        });
    },
    // 获取个人信息
    getUserMesssge(){
      var that = this
      if(this.userMessge.data.userInfo.companyId===null){
        that.formValidate.companyId = ''
      }else{
        that.formValidate.companyId = that.userMessge.data.userInfo.companyId
      }
      this.formValidate.company = that.userMessge.data.userInfo.companyName
      
      this.formValidate.realname = that.userMessge.data.userInfo.realName
      this.formValidate.sex = that.userMessge.data.userInfo.sex
      this.formValidate.idcard = that.userMessge.data.userInfo.idcard
      this.formValidate.birthdate = that.userMessge.data.userInfo.birthdayStr
      // this.formValidate.age = that.userMessge.data.userInfo.companyId
      this.formValidate.call = that.userMessge.data.userInfo.phone
      this.formValidate.email = that.userMessge.data.userInfo.email
      this.formValidate.idcardimg = that.userMessge.data.userInfo.idcardPhoto
      this.formValidate.Address = that.userMessge.data.userInfo.addr
    },
    handleReset(name) {
      this.$refs[name].resetFields();
    },
    uploadSuccess(res, file) {
      this.$Message.info("上传成功！");
      this.companyImg = res.path;
    },
    uploadSuccessId(res, file) {
      this.$Message.info("上传成功！");
      this.cardImg = res.path;
    },
    uploadError(res, file) {
      this.$Message.error("上传失败！" + res.data.msg);
    }
  }
};
</script>
<style scoped>
.information {
  width: 48%;
  margin-left: 2%;
}
.btns {
  text-align: right;
}
h3 {
  color: #ff0000;
}

</style>
