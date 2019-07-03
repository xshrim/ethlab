<!--侧边导航-->
<template>
	<div class="sider">
    <Menu ref="side_menu" :active-name="setActive" :open-names="[openActive]"  @on-select="routeTo" accordion width="auto">
      <Submenu :name="item.submenu"  v-for="(item ,index) in menuData" :key="index">
        <template slot="title">
          <Icon :type="item.icon" />
          {{item.message}}
        </template>
        <MenuItem :name="child.menuitem" v-for="child in item.children" :key="child.menuitem">
          <Icon :type="child.icon" />
          {{child.message}}
        </MenuItem>
      </Submenu>
    </Menu>
	</div>
</template>

<script>
export default {
  data() {
    return {
      imgsrc: domain.testUrl,
      username: "",
      setActive: "",
      openActive: "",
      menuData: [],
      menuDataList: [
        {
          submenu: "navUploading",
          icon: "ios-paper",
          message: "文件上传",
          children: [
            {
              menuitem: "Uploading",
              icon: "ios-paper",
              message: "文件上传/加密"
            }
          ]
        },
        {
          submenu: "navImpower",
          icon: "ios-people",
          message: "账户授权",
          children: [
            {
              menuitem: "Impower",
              icon: "ios-people",
              message: "账户授权"
            }
          ]
        },
        {
          submenu: "navRecord",
          icon: "md-list-box",
          message: "授权记录",
          children: [
            {
              menuitem: "Record",
              icon: "md-list-box",
              message: "授权记录"
            }
          ]
        },
        {
          submenu: "navBehavior",
          icon: "ios-paper-plane",
          message: "行为追踪",
          children: [
            {
              menuitem: "Behavior",
              icon: "ios-paper-plane",
              message: "行为追踪"
            }
          ]
        },
        {
          submenu: "allInformation",
          icon: "ios-information-circle",
          message: "信息管理",
          children: [
            // {
            //   menuitem: "Information",
            //   icon: "ios-information-circle-outline",
            //   message: "完善信息"
            // },
            {
              menuitem: "PersonInfor",
              icon: "md-information",
              message: "个人信息"
            }
          ]
        },
        {
          submenu: "allCheck",
          icon: "md-mail-open",
          message: "审核管理",
          children: [
            {
              menuitem: "ApplyList",
              icon: "md-mail-open",
              message: "申请列表"
            }
          ]
        },
        {
          submenu: "Chain",
          icon: "md-cube",
          message: "区块链管理",
          children: [
            {
              menuitem: "Monitoring",
              icon: "md-pulse",
              message: "监控信息"
            },
            {
              menuitem: "ChainDetails",
              icon: "md-repeat",
              message: "区块/交易信息"
            }
          ]
        }
      ]
    };
  },
  computed: {},

  mounted() {
    this.username = this.$store.state;
    this.getPersonInformation();
  },
  methods: {
    routeTo(e) {
      this.$router.push(e);
    },
    menuList() {
      // 这个方法里定义好，高亮和路由
      let path = this.$route.matched[1].path; // 获取到地址拦上#号后面的url地址
      if (path.indexOf("/Uploading") != -1) {
        // 是否包含，-1是包含，0不包含
        this.setActive = "Uploading";
        this.openActive = "navUploading";
      } else if (path.indexOf("/Impower") != -1) {
        this.setActive = "Impower";
        this.openActive = "navImpower";
      } else if (path.indexOf("/Record") != -1) {
        this.setActive = "Record";
        this.openActive = "navRecord";
      } else if (path.indexOf("/Behavior") != -1) {
        this.setActive = "Behavior";
        this.openActive = "navBehavior";
      } else if (path.indexOf("/Monitoring") != -1) {
        this.setActive = "Monitoring";
        this.openActive = "Chain";
      } else if (path.indexOf("/ApplyList") != -1) {
        this.setActive = "ApplyList";
        this.openActive = "allCheck";
      } else if (path.indexOf("/ChainDetails") != -1) {
        this.setActive = "ChainDetails";
        this.openActive = "Chain";
      } else if (path.indexOf("/PersonInfor") != -1) {
        this.setActive = "PersonInfor";
        this.openActive = "allInformation";
      }
      this.$nextTick(() => {
        this.$refs.side_menu.updateOpened();
        this.$refs.side_menu.updateActiveName();
      });
    },
    // 获取个人信息
    getPersonInformation() {
      var that = this;
      var param = new URLSearchParams();
      param.append("userName", this.username.user);
      this.$axios
        .post(this.imgsrc + "/rock/user/getUserInfo.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(res => {
          switch (res.data.userInfo.level) {
            // 根据角色不同配置不同导航栏
            case 0:
              this.menuData = [
                {
                  submenu: "allInformation",
                  icon: "ios-information-circle",
                  message: "信息管理",
                  children: [
                    {
                      menuitem: "PersonInfor",
                      icon: "md-information",
                      message: "个人信息"
                    }
                  ]
                }
              ];
              break;
            case 1:
              this.menuData = this.menuDataList;
              break;
            case 2:
              this.menuData = this.menuDataList.splice(0, 6);
              break;
            case 5:
              this.menuData = this.menuDataList.splice(0, 5);
              break;
            case 6:
              this.menuData = this.menuDataList.splice(0, 5);
              break;
          }
          this.menuList();
        })
        .catch(function(error) {
          that.$Message.error("获取消息失败！");
        });
    }
  }
};
</script>

<style scoped>
.ivu-menu-light {
  background: inherit !important;
}
.sider,
.ivu-menu-vertical {
  height: 100%;
}
</style>