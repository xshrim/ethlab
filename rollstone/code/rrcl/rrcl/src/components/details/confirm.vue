<!--提示页面 暂时无用-->
<template>
    <Row>
        <div class="wrapper">
            <h4>
                提示
            </h4>
            <p></p>
        </div>
    </Row>
</template>
<script>

    export default {  
        name: 'comfirm',
        data() {
            return {
                value1:'',
                value2:''
            }
        },

        methods:{
            value1Change:function() {
                var obj = this
                this.$emit('value1', obj.value1)
            },
            value2Change:function() {
                var obj = this
                this.$emit('value2', obj.value2)
            }
        } 
    }
</script>