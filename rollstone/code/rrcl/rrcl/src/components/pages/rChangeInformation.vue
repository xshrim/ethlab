<!--个人信息更改-->
<template>
    <div class="information">
        <div class="infor">
            <Tabs>
                <TabPane label="非认证信息更改" icon="ios-people">
                    <Form ref="formValidateF" :model="formValidateF" label-position="left" :label-width="100" :rules="ruleValidate">
                        <FormItem prop="birthdate" label="出生日期">
                            <DatePicker type="date" placeholder="选择日期" :value="formValidateF.birthdate" @on-change="birthdateValue"></DatePicker>
                        </FormItem>
                        <FormItem label="性别" prop="sex">
                            <RadioGroup v-model="formValidateF.sex">
                                <Radio :label="1">男</Radio>
                                <Radio :label="2">女</Radio>
                            </RadioGroup>
                        </FormItem>
                        <!-- <FormItem label="年龄" prop="age">
                            <Input v-model="formValidateF.age"></Input>
                        </FormItem> -->
                        <FormItem label="联系地址" prop="Address">
                            <Input v-model="formValidateF.Address"></Input>
                        </FormItem>
                        <FormItem label="联系电话" prop="call">
                            <Input v-model="formValidateF.call"></Input>
                        </FormItem>
                        <FormItem label="Email" prop="email">
                            <Input v-model="formValidateF.email"></Input>
                        </FormItem>
                    </Form>
                    <div class="btns">
                        <Button type="success" @click="handleReset('formValidateF')">重置</Button>
                        <Button type="primary" @click="handleSubmitF('formValidateF')" :disabled='btnDis' :loading = "buttonLoading">提交</Button>
                    </div>
                </TabPane>
                <TabPane label="认证信息更改" icon="ios-person">
                    <Form ref="formValidateR" :model="formValidateR" label-position="left" :label-width="100" :rules="ruleValidate">
                        <FormItem label="姓名" prop="realname">
                            <Input v-model="formValidateR.realname" :readonly = "isCheck"></Input>
                        </FormItem>
                        <FormItem label="身份证号" prop="idcard">
                            <Input v-model="formValidateR.idcard" :readonly = "isCheck"></Input>
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
                        <FormItem label="公司名称" prop="company">
                            <Input v-model="formValidateR.company" :readonly = "isCheck"></Input>
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
                            <Select v-model="formValidateR.companyId" placeholder="请选择您所属的公司" :disabled = "isCheck">
                                <Option  v-for="item in companyList" :key="item.id" :value="item.id" >{{item.name}}</Option>
                            </Select>
                        </FormItem>
                        <FormItem label="用户级别" prop="level">
                            <RadioGroup v-model="formValidateR.level">
                                <Radio :label="2" :disabled = "isCheck">企业管理员</Radio>
                                <Radio :label="5" :disabled = "isCheck">企业员工</Radio>
                                <Radio :label="6" :disabled = "isCheck">普通用户</Radio>
                            </RadioGroup>
                        </FormItem>
                    </Form>
                    <div class="btns">
                        <Button type="success" @click="handleReset('formValidateR')">重置</Button>
                        <Button type="primary" @click="handleSubmitR('formValidateR')" :disabled='btnDis' :loading = "buttonLoading">提交</Button>
                    </div>
                </TabPane>
            </Tabs>
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
      isCheck: false,
      buttonLoading: false,
      companyList: "",
      action1: domain.testUrl + "/rock/file/upload.action",
      action2: domain.testUrl + "/rock/file/upload.action",
      formValidateF: {
        Address: "",
        sex: "",
        birthdate: "",
        // age: '',
        call: "",
        email: ""
      },
      formValidateR: {
        realname: "",
        idcard: "",
        level: 2,
        company: "",
        companyId: "",
        qualification: ""
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
        // company: [
        //     { required: true, message: '公司名称不能为空', trigger: 'blur' }
        // ],
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
  components: {},
  mounted() {
    this.userId = this.$store.state.token;
    this.getCompany();
    this.getChangeInformation();
  },
  methods: {
    birthdateValue(e) {
      this.formValidateF.birthdate = e;
    },
    handleSubmitF(name) {
      var that = this;
      this.$refs[name].validate(valid => {
        if (valid) {
          that.buttonLoading = true;
          var param = new URLSearchParams();
          param.append("userId", that.userId);
          param.append("email", that.formValidateF.email);
          param.append("phone", that.formValidateF.call);
          param.append("addr", that.formValidateF.Address);
          param.append("birthdayStr", that.formValidateF.birthdate);
          param.append("sex", that.formValidateF.sex);
          param.append("type", 1);
          this.$axios
            .post(that.imgsrc + "/rock/user/modifyUserInfo.action", param, {
              xhrFields: {
                withCredentials: true
              }
            })
            .then(function(res) {
              if (res.data.code === "0") {
                that.btnDis = true;
                that.buttonLoading = false;
                that.$Message.info("修改成功!");
                that.reload();
              } else if (res.data.code === "-1") {
                that.$Message.error("提交失败!" + res.data.msg);
                that.buttonLoading = false;
              }
            })
            .catch(function(error) {
              that.$Message.error("提交失败！" + error);
              that.buttonLoading = false;
            });
        } else {
          that.$Message.error("请填写完整信息!");
          that.buttonLoading = false;
        }
      });
    },
    handleSubmitR(name) {
      var that = this;
      this.$refs[name].validate(valid => {
        if (valid) {
          that.buttonLoading = true;
          var param = new URLSearchParams();
          param.append("realName", that.formValidateR.realname);
          param.append("idcard", that.formValidateR.idcard);
          param.append("idcardPhoto", that.cardImg);
          param.append("level", that.formValidateR.level);
          param.append("companyName", that.formValidateR.company);
          param.append("companyApt", that.formValidateR.qualification);
          param.append("companyId", that.formValidateR.companyId);
          param.append("type", 2);
          this.$axios
            .post(that.imgsrc + "/rock/user/modifyUserInfo.action", param, {
              xhrFields: {
                withCredentials: true
              }
            })
            .then(function(res) {
              if (res.data.code === "0") {
                that.btnDis = true;
                that.buttonLoading = false;
                that.$Message.info(
                  "提交成功!请耐心等待审核，审核时间为1~3天！"
                );
                that.reload();
              } else if (res.data.code === "-1") {
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
          that.buttonLoading = false;
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
    // 获取修改用户资料页用户信息
    getChangeInformation() {
      var that = this;
      this.$axios
        .get(
          this.imgsrc + "/rock/auth/getAuthUserInfo.action",
          {},
          {
            xhrFields: {
              withCredentials: true
            }
          }
        )
        .then(function(res) {
          if (res.data.status === 1) {
            that.isCheck = true;
          } else {
            that.isCheck = false;
          }
          that.userId = res.data.userInfo.userId;
          that.formValidateF.Address = res.data.userInfo.addr;
          that.formValidateF.sex = res.data.userInfo.sex;
          that.formValidateF.birthdate = res.data.userInfo.birthdayStr;
          // that.formValidateF.age = res.data.userInfo
          that.formValidateF.call = res.data.userInfo.phone;
          that.formValidateF.email = res.data.userInfo.email;

          that.formValidateR.realname = res.data.userInfo.realName;
          that.formValidateR.idcard = res.data.userInfo.idcard;
          that.cardImg = res.data.userInfo.idcardPhoto;
          that.formValidateR.level = res.data.userInfo.level;
          if (res.data.userInfo.companyName === null) {
            that.formValidateR.company = "";
          }
          that.formValidateR.companyId = res.data.userInfo.companyId;
          that.formValidateR.qualification = res.data.userInfo.companyApt;
        })
        .catch(function(error) {
          that.$Message.error("提交失败！" + error);
        });
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
.infor {
  width: 98%;
  margin: 0 auto;
}
</style>
