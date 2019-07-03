<!--资源上传页面-->
<template>
    <div class="fileUploading">
        <div class="fileInput">
            <Upload
                ref="file"
                :with-credentials= true
                :on-success="uploadSuccess"
                :on-error="uploadError"
                name="file"
                multiple
                type="drag"
                :format="['jpg','jpeg','png','mp4','mp3']"
                :max-size="20480000"
                :before-upload="handleUpload"
                :action="action">
                <div style="padding: 20px 0">
                    <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                    <p>Click or drag files here to upload</p>
                </div>
            </Upload>
        </div> 
        <Row>
            <Col span="2">文件标题：</Col>
            <Col span="22"><Input v-model="fileTitle" placeholder="输入资源名称" /></Col>
        </Row>
        <Row>
            <Col span="2">文件描述：</Col>
            <Col span="22"><Input v-model="fileDescript" type="textarea" placeholder="输入资源描述信息" /></Col>
        </Row>
        <Row>
            <Col span="2">文件类型：</Col>
            <Col span="20">
                <RadioGroup v-model="fileType">
                    <Radio label="1">视频文件</Radio>
                    <Radio label="2">文本文件</Radio>
                </RadioGroup>
            </Col>
            <Col span="2"><Button type="primary" :loading="buttonLoading" @click="submitFile">{{this.butText}}</Button></Col>
        </Row>
    </div>
</template>

<script>
import confirm from "../details/confirm";
export default {
  inject: ["reload"],
  data() {
    return {
      imgsrc: domain.testUrl,
      buttonLoading: false,
      butText: "提交",
      fileTitle: "",
      fileDescript: "",
      fileType: "1",
      filePath: "",
      action: domain.testUrl + "/rock/file/upload.action"
    };
  },
  components: {
    confirm
  },
  methods: {
    handleUpload() {},
    uploadSuccess(res, file) {
      this.$Message.info("上传成功！");
      this.filePath = res.path;
    },
    uploadError(res, file) {
      this.$Message.error("上传失败！" + res.data.msg);
    },
    submitFile() {
      var that = this;
      if (this.filePath === "" || null) {
        this.$Message.error("请上传视频或文件！");
        return false;
      } else {
        this.buttonLoading = true;
        this.butText = "Loading";
        var param = new URLSearchParams();
        param.append("title", this.fileTitle);
        param.append("content", this.fileDescript);
        param.append("type", this.fileType);
        param.append("path", this.filePath);
        this.$axios
          .post(this.imgsrc + "/rock/item/addItem.action", param, {
            xhrFields: {
              withCredentials: true
            }
          })
          .then(res => {
            if (res.data.code === 0) {
              that.$Message.info("提交成功！");
              this.buttonLoading = false;
              this.butText = "提交";
              this.reload();
            } else if (res.data.code === -1) {
              that.$Message.error("提交失败!" + res.data.msg);
              this.buttonLoading = false;
              this.butText = "提交";
            }
          })
          .catch(function(error) {
            that.$Message.error("提交失败！" + error);
            this.buttonLoading = false;
            this.butText = "提交";
          });
      }
    }
  }
};
</script>

<style scoped>
.fileInput {
  margin-top: 15px;
}
.ivu-row {
  margin: 10px 0;
}
</style>