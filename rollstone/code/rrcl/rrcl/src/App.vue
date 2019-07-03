<template>
  <div id="app">
    <router-view v-if="isRouterAlive" />
  </div>
</template>

<script>
export default {
  name: 'App',
  provide(){
    return{
      reload:this.reload
    }
  },
  data(){
    return{
      isRouterAlive:true
    }
  },
  methods:{
    reload(){
      this.isRouterAlive= false
      this.$nextTick(function(){
        this.isRouterAlive = true
      })
    }
  }
}
</script>

<style>
html,body,#app{
  height: 100%;
  width: 100%;
  background-color: #fffed0;
}

.ivu-table th {
    height: 40px;
    white-space: nowrap;
    overflow: hidden;
    background-color: #2d8cf0;
    color: #fff;
}
.ivu-table-row .ivu-table-cell {
    padding-left: 18px;
    padding-right: 18px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: normal;
    box-sizing: border-box;
    word-break: inherit;
}

.ivu-menu-horizontal {
    height: 35px;
    line-height: 22px;
}

.fileUploading .ivu-input,
.information .ivu-input,
.information .ivu-btn-default{
    background-color: inherit;
    color: #2d8cf0;
}

.ivu-table,
.ivu-table td,
.ivu-radio-inner,
.ivu-upload-drag,
.ivu-select-selection,
.ivu-menu-light{
  background-color: inherit;
}
</style>
