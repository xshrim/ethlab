<!--重新认证信息-员工-->
<template>
  <div class="information">
    <div class="addInfo ">
        <Form ref="formValidate" :model="formValidate" label-position="left" :label-width="100" :rules="ruleValidate">
            <FormItem label="姓名" prop="realname">
                <Input v-model="formValidate.realname"></Input>
            </FormItem>
            <FormItem label="身份证号" prop="idcard">
                <Input v-model="formValidate.idcard"></Input>
            </FormItem>
            <FormItem label="">
                <div>
                    <Upload
                        ref="file"
                        :with-credentials= true
                        :on-success="uploadSuccessId"
                        :on-error="uploadError"
                        name="file"
                        :action="action"
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
            <FormItem label="联系电话" prop="call">
                <Input v-model="formValidate.call"></Input>
            </FormItem>
            <FormItem label="Email" prop="email">
                <Input v-model="formValidate.email"></Input>
            </FormItem>
            <FormItem label="联系地址" prop="address">
                <Input v-model="formValidate.address"></Input>
            </FormItem>
            <FormItem label="选择企业/公司" prop="selectValue">
                <Select v-model="formValidate.selectValue" placeholder="请选择您所属的公司">
                    <Option  v-for="item in companyList" :key="item.id" :value="item.id">{{item.name}}</Option>
                </Select>
            </FormItem>
            <FormItem label="用户级别" prop="level">
                <RadioGroup v-model="formValidate.level">
                    <Radio :label="5">企业员工</Radio>
                </RadioGroup>
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
      userMessge:'',
      credentials: true,
      btnDis: false,
      buttonLoading: false,
      idcardimg: "",
      userId: "",
      companyList: "",
      action: domain.testUrl + "/rock/file/upload.action",
      formValidate: {
        selectValue: "",
        realname: "",
        sex: "",
        idcard: "",
        birthdate: "",
        age: "",
        call: "",
        email: "",
        address: "",
        level: 5
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
        call: [
          { required: true, message: "联系电话不能为空", trigger: "blur" }
        ],
        email: [{ required: true, message: "邮箱不能为空", trigger: "blur" }],
        // selectValue: [
        //     { required: true, message: '企业/公司不能为空', trigger: 'change'}
        // ],
        address: [{ required: true, message: "地址不能为空", trigger: "blur" }],
        level: [{ required: true, message: "请选择用户级别", trigger: "blur" }]
      }
    };
  },
  created() {
    this.userId = this.$store.state.token;
    this.userMessge = this.$route.query.params
    this.getCompany();
    this.getUserMesssge()
  },
  methods: {
    birthdateValue(e) {
      this.formValidate.birthdate = e;
    },
    handleSubmit(name) {
      var that = this;
      this.$refs[name].validate(valid => {
        if (valid) {
          that.buttonLoading = true;
          var param = new URLSearchParams();
          param.append("userId", this.userId);
          param.append("email", this.formValidate.email);
          param.append("realName", this.formValidate.realname);
          param.append("idcard", this.formValidate.idcard);
          param.append("idcardPhoto", this.idcardimg);
          param.append("addr", this.formValidate.address);
          param.append("phone", this.formValidate.call);
          param.append("birthdayStr", this.formValidate.birthdate);
          param.append("sex", this.formValidate.sex);
          param.append("level", this.formValidate.level);
          param.append("companyId", this.formValidate.selectValue);
          this.$axios
            .post(this.imgsrc + "/rock/auth/submitInfo.action", param, {
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
                that.buttonLoading = false;
                //跳到目标页
                that.reload();
                that.$router.push("/Home");
                return false;
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
    handleReset(name) {
      this.$refs[name].resetFields();
    },
    uploadSuccessId(res, file) {
      this.$Message.info("上传成功！");
      this.idcardimg = res.path;
    },
    uploadError(res, file) {
      this.$Message.error("上传失败！" + res.data.msg);
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
            that.$Message.error("获取数据失败!" + res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error("获取数据失败！" + error);
        });
    },
    // 获取个人信息
    getUserMesssge(){
      var that = this
      this.formValidate.selectValue = that.userMessge.data.userInfo.companyId
      this.formValidate.realname = that.userMessge.data.userInfo.realName
      this.formValidate.sex = that.userMessge.data.userInfo.sex
      this.formValidate.idcard = that.userMessge.data.userInfo.idcard
      this.formValidate.birthdate = that.userMessge.data.userInfo.birthdayStr
      // this.formValidate.age = that.userMessge.data.userInfo.companyId
      this.formValidate.call = that.userMessge.data.userInfo.phone
      this.formValidate.email = that.userMessge.data.userInfo.email
      this.formValidate.idcardimg = that.userMessge.data.userInfo.idcardPhoto
      this.formValidate.address = that.userMessge.data.userInfo.addr
    }
  }
};
</script>
<style>
.btns {
  text-align: right;
}
.addInfo{
  width: 50%;
} 
</style>
