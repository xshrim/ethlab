<template>
	<div class="header">
		<Row>
			<Col span="10">
				<div class="logo">
					<img src="../assets/timg.png" alt="加载失败" >
				</div>
				<h3>滚石国际音乐有限公司</h3>	
			</Col>
			<Col span="14">
				<div class="headLogo">
					<Avatar src="http://www.qqzhi.com/uploadpic/2015-01-15/202249542.jpg" />
					<Dropdown trigger="click" style="margin-left: 20px">
							<a href="javascript:void(0)">
									欢迎 <strong>{{admin}}</strong>！
									<Icon type="arrow-down-b"></Icon>
							</a>
							<DropdownMenu slot="list">
									<DropdownItem>
										<a href="javascript:void(0)" @click="logout" class="liItem">
											退出登录
										</a>
									</DropdownItem>
							</DropdownMenu>
					</Dropdown>
				</div>
			</Col>
		</Row>
	</div>
</template>

<script>
export default {
  data() {
    return {
      imgsrc: domain.testUrl,
      users: "",
      admin: ""
    };
  },
  created() {
    this.admin = this.$store.state.user;
  },
  methods: {
    logout() {
      var that = this;
      this.$axios
        .get(
          this.imgsrc + "/rock/logout.action",
          {},
          {
            xhrFields: {
              withCredentials: true
            }
          }
        )
        .then(res => {
          if (res.data.code === 0) {
            that.$Message.info("退出成功！");
            //清除token
            that.$store.dispatch("UserLogout");
            that.$router.push("/Login");
          } else if (res.data.code === -1) {
            // that.$Message.error("退出失败!" + res.data.msg);
            //清除token
            that.$store.dispatch("UserLogout");
            that.$router.push("/Login");
          }
        })
        .catch(function(error) {
          that.$Message.error("退出失败！" + error);
        });
    }
  }
};
</script>

<style scoped>
.header {
  position: relative;
  z-index: 999;
}
img {
  width: 100%;
}
.logo {
  width: 50px;
  height: 41px;
  display: inline-block;
  vertical-align: middle;
}
.ivu-dropdown-rel a {
  color: #fff;
}
h3 {
  display: inline-block;
  vertical-align: middle;
  font-size: 20px;
}
.headLogo {
  text-align: right;
}
.ivu-dropdown-item {
  text-align: center;
}
.liItem {
  display: block;
  width: 100%;
  height: 100%;
}
</style>