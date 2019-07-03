<template>
  <div class="login">
        <vue-particles
        color="#fff"
        :particleOpacity="0.7"
        :particlesNumber="60"
        shapeType="circle"
        :particleSize="4"
        linesColor="#fff"
        :linesWidth="1"
        :lineLinked="true"
        :lineOpacity="0.4"
        :linesDistance="150"
        :moveSpeed="2"
        :hoverEffect="true"
        hoverMode="grab"
        :clickEffect="true"
        clickMode="push"
        class="lizi"
      >
      </vue-particles>
      <keep-alive>
        <router-view></router-view>
      </keep-alive>
  </div>
</template>

<script>

</script>
<style>
h2{
  text-align: center;
  color: #fff;
  padding-bottom: 15px;  
}
.login{
  height: 100%;
  overflow: hidden;
  background-color: #000;
}
.loginBox {
  width: 30%;
  position: absolute;
  top: 20%;
  left: 35%;
  border-radius: 5px;
  padding: 20px;
  background-color:rgba(255, 255, 255, 0.3);
}

.loginBox button {
  margin-bottom: 15px;
}
.register,.forger,.outRegirt {
  display: block;
  color:#fff;
  text-align: left;
}
.forger,.outRegirt{
  text-align: right;
}
.ivu-input-type .ivu-input-icon {
  left: 0;
}
.ivu-input-type .ivu-input-icon-normal + .ivu-input {
  padding-left: 32px;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.05s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
.registerBox .ivu-checkbox-wrapper {
  margin-bottom: 10px;
  color: #fff;
}
.loginBox .ivu-input-type .ivu-input-icon-normal + .ivu-input,.loginBox .ivu-input{
  background-color: inherit;
  color: #fff;
}
.information .ivu-input-type .ivu-input-icon-normal + .ivu-input,.information .ivu-input{
    color: #2d8cf0
}
</style>
