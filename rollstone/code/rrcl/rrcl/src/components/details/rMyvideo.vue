<!--我的视频列表-->
<template>
    <div>
        <Table border :columns="columns" :data="data"></Table>
        <Page :total="100" show-elevator></Page>
    </div>
</template>
<script>
import axios from "axios";
import Add from "./rAdd.vue";
import Video from "./video.vue";

export default {
  data() {
    return {
      modal: false,
      imgsrc: domain.testUrl,
      tid: "",
      itemId: "",
      otherValueR: "",
      equipmentValueR: "",
      watermarkValueR: "",
      limitsValueR: [],
      playTimeValueR: "",
      playNumValueR: "",
      starValueR: "",
      stopValueR: "",
      overValueR: "",
      typeValueR: "0",
      params: "",
      videoParams: "",
      videoPathURL: "",
      videoPath: "",
      columns: [
        {
          title: "标题",
          key: "title"
        },
        {
          title: "资源上传者",
          key: "uperId"
        },
        {
          title: "资源所有者",
          key: "userId"
        },
        {
          title: "上传时间",
          key: "timestamp"
        },
        {
          title: "资源类型",
          key: "type"
        },
        {
          title: "Action",
          key: "action",
          width: 250,
          align: "center",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.showDetails(params.index);
                    }
                  }
                },
                "查看"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.openVideo(params);
                    }
                  }
                },
                "播放"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.getDownload(params);
                    }
                  }
                },
                "下载"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.openModal(params);
                    }
                  }
                },
                "授权"
              )
            ]);
          }
        }
      ],
      data: []
    };
  },
  components: {
    Add,
    Video
  },
  mounted() {
    this.getData();
  },
  methods: {
    // 详情
    showDetails(index) {
      this.$Modal.info({
        title: "基本信息",
        content: ` 资源标题：${this.data[index].title}<br>资源类型：${
          this.data[index].type
        }<br>资源描述：${this.data[index].content}`
      });
    },
    // 下载
    getDownload(params) {
      this.videoParams = params;
      let url = this.videoPath + params.row.path;
      console.log(typeof(url))
      this.downjs(url,"demo.mp4","video/mp4")
    // var eleLink = document.createElement('a');
    // eleLink.download = filename;
    // eleLink.style.display = 'none';
    // // 字符内容转变成blob地址
    // // var blob = new Blob([content]);
    // // eleLink.href = URL.createObjectURL(blob);
    // eleLink.href = content
    // // 触发点击
    // document.body.appendChild(eleLink);
    // eleLink.click();
    // // 然后移除
    // document.body.removeChild(eleLink);  
    // function download(url, params) {
    //   let tempForm = document.createElement('form')
    //   tempForm.action = url
    //   tempForm.method = 'get'
    //   tempForm.style.display = 'none'
    //   for (var x in params) {
    //     let opt = document.createElement('textarea')
    //     opt.name = x
    //     opt.value = params[x]
    //     tempForm.appendChild(opt)
    //   }
    //   document.body.appendChild(tempForm)
    //   tempForm.submit()
    //   return tempForm
    // }
    // const url = 'ali-oss-object-url'
    // download(url)
    },
    downjs(data, strFileName, strMimeType){
        var self = window, // this script is only for browsers anyway...
            defaultMime = "application/octet-stream", // this default mime also triggers iframe downloads
            mimeType = strMimeType || defaultMime,
            payload = data,
            url = !strFileName && !strMimeType && payload,
            anchor = document.createElement("a"),
            toString = function(a) { return String(a); },
            myBlob = (self.Blob || self.MozBlob || self.WebKitBlob || toString),
            fileName = strFileName || "download",
            blob,
            reader;
        myBlob = myBlob.call ? myBlob.bind(self) : Blob;

        if (String(this) === "true") { //reverse arguments, allowing download.bind(true, "text/xml", "export.xml") to act as a callback
            payload = [payload, mimeType];
            mimeType = payload[0];
            payload = payload[1];
        }


        if (url && url.length < 2048) { // if no filename and no mime, assume a url was passed as the only argument
            fileName = url.split("/").pop().split("?")[0];
            anchor.href = url; // assign href prop to temp anchor
            if (anchor.href.indexOf(url) !== -1) { // if the browser determines that it's a potentially valid url path:
                var ajax = new XMLHttpRequest();
                ajax.open("GET", url, true);
                ajax.responseType = 'blob';
                ajax.onload = function(e) {
                    download(e.target.response, fileName, defaultMime);
                };
                setTimeout(function() { ajax.send(); }, 0); // allows setting custom ajax headers using the return:
                return ajax;
            } // end if valid url?
        } // end if url?


        //go ahead and download dataURLs right away
        if (/^data\:[\w+\-]+\/[\w+\-]+[,;]/.test(payload)) {

            if (payload.length > (1024 * 1024 * 1.999) && myBlob !== toString) {
                payload = dataUrlToBlob(payload);
                mimeType = payload.type || defaultMime;
            } else {
                return navigator.msSaveBlob ? // IE10 can't do a[download], only Blobs:
                    navigator.msSaveBlob(dataUrlToBlob(payload), fileName) :
                    saver(payload); // everyone else can save dataURLs un-processed
            }

        } //end if dataURL passed?

        blob = payload instanceof myBlob ?
            payload :
            new myBlob([payload], { type: mimeType });


        function dataUrlToBlob(strUrl) {
            var parts = strUrl.split(/[:;,]/),
                type = parts[1],
                decoder = parts[2] == "base64" ? atob : decodeURIComponent,
                binData = decoder(parts.pop()),
                mx = binData.length,
                i = 0,
                uiArr = new Uint8Array(mx);

            for (i; i < mx; ++i) uiArr[i] = binData.charCodeAt(i);

            return new myBlob([uiArr], { type: type });
        }

        function saver(url, winMode) {

            if ('download' in anchor) { //html5 A[download]
                anchor.href = url;
                anchor.setAttribute("download", fileName);
                anchor.className = "download-js-link";
                anchor.innerHTML = "downloading...";
                anchor.style.display = "none";
                document.body.appendChild(anchor);
                setTimeout(function() {
                    anchor.click();
                    document.body.removeChild(anchor);
                    if (winMode === true) { setTimeout(function() { self.URL.revokeObjectURL(anchor.href); }, 250); }
                }, 66);
                return true;
            }

            // handle non-a[download] safari as best we can:
            if (/(Version)\/(\d+)\.(\d+)(?:\.(\d+))?.*Safari\//.test(navigator.userAgent)) {
                url = url.replace(/^data:([\w\/\-\+]+)/, defaultMime);
                if (!window.open(url)) { // popup blocked, offer direct download:
                    if (confirm("Displaying New Document\n\nUse Save As... to download, then click back to return to this page.")) { location.href = url; }
                }
                return true;
            }

            //do iframe dataURL download (old ch+FF):
            var f = document.createElement("iframe");
            document.body.appendChild(f);

            if (!winMode) { // force a mime that will download:
                url = "data:" + url.replace(/^data:([\w\/\-\+]+)/, defaultMime);
            }
            f.src = url;
            setTimeout(function() { document.body.removeChild(f); }, 333);

        } //end saver




        if (navigator.msSaveBlob) { // IE10+ : (has Blob, but not a[download] or URL)
            return navigator.msSaveBlob(blob, fileName);
        }

        if (self.URL) { // simple fast and modern way using Blob and URL:
            saver(self.URL.createObjectURL(blob), true);
        } else {
            // handle non-Blob()+non-URL browsers:
            if (typeof blob === "string" || blob.constructor === toString) {
                try {
                    return saver("data:" + mimeType + ";base64," + self.btoa(blob));
                } catch (y) {
                    return saver("data:" + mimeType + "," + encodeURIComponent(blob));
                }
            }

            // Blob but not URL support:
            reader = new FileReader();
            reader.onload = function(e) {
                saver(this.result);
            };
            reader.readAsDataURL(blob);
        }
        return true;
    },
    // 获取列表数据
    getData() {
      var that = this;
      var param = new URLSearchParams();
      param.append("type", 1);
      this.$axios
        .post(this.imgsrc + "/rock/item/getItemList.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(function(res) {
          if (res.data.code === 0) {
            if (res.data.list === null) {
              res.data.list = [];
            } else {
              for (var i in res.data.list) {
                var newTime = new Date(
                  res.data.list[i].timestamp * 1000
                ).toLocaleString();
                res.data.list[i].timestamp = newTime;
                if (res.data.list[i].type === 1) {
                  res.data.list[i].type = "视频文件";
                } else {
                  res.data.list[i].type = "文本文件";
                }
              }
              that.data = res.data.list;
              that.videoPath = res.data.preFix;
            }
            // this.reload();
          } else if (res.data.code === -1) {
            that.$Message.error("获取数据失败!" + res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error("获取数据失败！" + error);
        });
    },
    // 筛选数组
    IsInArray(arr, val) {
      var testStr = "," + arr.join(",") + ",";
      return testStr.indexOf("," + val + ",") != -1;
    },
    // 授权弹窗
    openModal(params) {
      var that = this;
      that.params = params;
      this.$Modal.confirm({
        scrollable: true,
        okText: "提交",
        render: h => {
          return h(Add, {
            props: {},
            on: {
              otherValueR: otherValue => {
                this.otherValueR = otherValue;
              },
              equipmentValueR: equipmentValue => {
                this.equipmentValueR = equipmentValue;
              },
              watermarkValueR: watermarkValue => {
                this.watermarkValueR = watermarkValue;
              },
              limitsValueR: limitsValue => {
                this.limitsValueR = limitsValue;
              },
              playTimeValueR: playTimeValue => {
                this.playTimeValueR = playTimeValue;
              },
              playNumValueR: playNumValue => {
                this.playNumValueR = playNumValue;
              },
              starValueR: starValue => {
                this.starValueR = starValue;
              },
              stopValueR: stopValue => {
                this.stopValueR = stopValue;
              },
              overValueR: overValue => {
                this.overValueR = overValue;
              },
              typeValueR: typeValue => {
                this.typeValueR = typeValue;
              }
            }
          });
        },
        onOk: () => {
          var that = this;
          if (this.playTimeValueR === "" || null) {
            var newPlayTime = [-1, -1];
          } else {
            var newPlayTime = [this.playTimeValueR, this.playTimeValueR];
          }
          if (this.playNumValueR === "" || null) {
            var newPlayNum = [-1, -1];
          } else {
            var newPlayNum = [this.playNumValueR, this.playNumValueR];
          }
          if (this.starValueR === "" || null) {
            this.starValueR = -1;
          }
          if (this.stopValueR === "" || null) {
            this.stopValueR = -1;
          }
          var thisTime = this.overValueR;
          thisTime = thisTime.replace(/-/g, "/");
          var newTime = new Date(thisTime);
          newTime = newTime.getTime() / 1000;
          if (this.overValueR === "" || null) {
            newTime = -1;
          }
          var limitsArry = [];
          if (this.IsInArray(this.limitsValueR, "play")) {
            limitsArry.push(1);
          } else {
            limitsArry.push(0);
          }
          if (this.IsInArray(this.limitsValueR, "downy")) {
            limitsArry.push(1);
          } else {
            limitsArry.push(0);
          }
          if (this.IsInArray(this.limitsValueR, "dowmj")) {
            limitsArry.push(1);
          } else {
            limitsArry.push(0);
          }
          if (this.IsInArray(this.limitsValueR, "sq")) {
            limitsArry.push(1);
          } else {
            limitsArry.push(0);
          }
          var param = new URLSearchParams();
          param.append("userId", this.otherValueR);
          param.append("tid", params.row.tid);
          param.append("itemId", params.row.iid);
          param.append("device", this.equipmentValueR);
          param.append("pmark", this.watermarkValueR);
          param.append("prvs", limitsArry);
          param.append("ptime", newPlayTime);
          param.append("ptimes", newPlayNum);
          param.append("psliceStart", this.starValueR);
          param.append("psliceEnd", this.stopValueR);
          param.append("ptimestampEnd", newTime);
          param.append("ptype", this.typeValueR);
          this.$axios
            .post(this.imgsrc + "/rock/perm/addPerm.action", param, {
              xhrFields: {
                withCredentials: true
              }
            })
            .then(function(res) {
              if (res.data.code === 0) {
                that.$Message.info("提交成功!");
                // this.reload();
              } else if (res.data.code === -1) {
                that.$Message.error("提交失败!" + res.data.msg);
              }
            })
            .catch(function(error) {
              that.$Message.error("提交失败！" + error);
            });
        }
      });
    },
    //视频弹窗
    openVideo(params) {
      var that = this;
      this.videoParams = params;
      this.videoPathURL = this.videoPath + this.videoParams.row.path;
      that.$Modal.confirm({
        scrollable: true,
        render: h => {
          return h(Video, {
            props: {
              videoParams: that.videoParams,
              videoPathURL: that.videoPathURL
            },
            on: {
              videoEndTime: statTime => {
                that.videoEndTime = statTime;
              }
            }
          });
        },
        onOk: () => {
          // that.PlayerPlay("stop", that.videoEndTime);
        }
      });
    }
  }
};
</script>
<style scoped>
.ivu-page {
  text-align: center;
  margin: 15px 0;
}
</style>
