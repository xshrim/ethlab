<!--监控页面添加节点-->
<template>
    <div class="">
        <h4>节点格式：IP+端口号</h4>
        <Input v-model="value" @on-change="valueChange"></Input>
    </div>
</template>
<script>
    export default {  
        name: 'add',
        data() {
            return {
                value:'',
            }
        },
        methods:{
            valueChange:function() {
                var obj = this
                this.$emit('value', obj.value)
            },
        } 
    }
</script>
<style scoped>
h4{
    margin-bottom: 15px;
}
</style>
