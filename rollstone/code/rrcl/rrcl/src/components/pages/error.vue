<!--出错页面  暂时无用-->
<template>
    <div class="error">
        <h1>哎呀！出错了！</h1>
        <button type="button" @click="goHome()">重新登录</button>
    </div>
</template>
<script>
export default {
    data(){
        return{

        }
    },
    methods:{
        goHome:function(){
            this.$router.replace({path:'/Login'})
        }
    }
}
</script>
<style scoped>
    .error{
        text-align: center;
    }
</style>

