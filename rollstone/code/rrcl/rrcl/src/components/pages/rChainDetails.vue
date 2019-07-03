<!--区块信息-->
<template>
    <div class="chainDetails">
        <Table :loading="loading" :columns="columns" :data="data" stripe></Table>
        <Page :total="pageTotal" :current="pageNum" :page-size="pageSize"  @on-change="handlePage"  show-elevator show-total></Page>
    </div>
</template>
<script>
import Vue from "vue";
import VueSocketio from "vue-socket.io";
import socketio from "socket.io-client";

Vue.use(VueSocketio, socketio("http://172.16.201.189:8000/mio"));
export default {
  data() {
    return {
      loading: true,
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      columns: [
        {
          title: "区块号",
          key: "number",
          width: 80
        },
        {
          title: "块Hash",
          key: "hash",
          width: 500
        },
        {
          title: "区块大小",
          key: "size"
        },
        {
          title: "产生时间",
          key: "timestamp"
        },
        {
          title: "操作",
          key: "action",
          width: 100,
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
                      this.href(params.index);
                    }
                  }
                },
                "详情"
              )
            ]);
          }
        }
      ],
      data: []
    };
  },
  sockets: {
    //将（socket.on）绑定事件放在sockets中
    connect: function() {
      console.log("socket connected");
    },
    connect_error: function(error) {
      this.$router.push({ path: "/Error" });
      this.$socket.disconnect();
    },
    // 获取区块总数
    view_chainInfo: function(val) {
      this.pageTotal = val.msg.blockNumber;
    },
    get_blocks(val) {
      this.data = val.msg.reverse();
      for (var i in this.data) {
        var newTime = new Date(this.data[i].timestamp * 1000).toLocaleString();
        this.data[i].timestamp = newTime;
      }
      this.loading = false;
    }
  },
  created() {
    this.getBlocks();
  },
  mounted() {},
  methods: {
    href(index) {
      this.$router.push({
        path: "/BlockTrade",
        query: { hash: this.data[index].hash }
      });
    },
    remove(index) {
      this.data6.splice(index, 1);
    },
    // 获取所有区块信息
    getBlocks() {
      var item = {
        start: this.pageTotal - 9,
        end: "latest"
      };
      this.$socket.emit("getBlocks", item);
    },
    handlePage(val) {
      if (val == 1) {
        var item = {
          start: this.pageTotal - 9,
          end: "latest"
        };
      } else {
        var starPage = 1 + this.pageTotal - val * 10;
        if (starPage <= 0) {
          var item = {
            start: 1,
            end: 10 + this.pageTotal - val * 10
          };
        } else {
          var item = {
            start: starPage,
            end: 10 + this.pageTotal - val * 10
          };
        }
      }
      this.$socket.emit("getBlocks", item);
    }
  }
};
</script>

<style>
.ivu-page {
  text-align: center;
  margin: 15px 0;
}
</style>
