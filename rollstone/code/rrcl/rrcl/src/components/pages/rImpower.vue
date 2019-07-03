<!--视频区域-->
<template>
    <div class="impower">
        <div class="navImpower">
            <Menu mode="horizontal" :active-name="Active" active-name="myVideo" @on-select="routeTo">
                <MenuItem name="myVideo">
                    <Icon type="ios-paper"></Icon>
                    我的视频
                </MenuItem>
                <MenuItem name="impowerVideo">
                    <Icon type="ios-people"></Icon>
                    授权的视频
                </MenuItem>
            </Menu>
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    export default{
        data(){
            return {
                page: ['Myvideo','Impowervideo']
            }
        },
        computed:{
            Active(){
                return this.$route.path.replace('/','myVideo');
            }
            
        },
        created(){
            this.checkNav()
        },
        methods:{
            routeTo(e) {
                this.$router.push(e);
            },
            checkNav(){
                let path = this.$route.matched[1].path  // 获取到地址拦上#号后面的url地址
                if(path.indexOf('/impowerVideo') != -1){  // 是否包含，-1是包含，0不包含
                    // return this.$route.path.replace('/','impowerVideo');
                } 
            }
        }
    }    
</script>

<style>
.myVideo,
impowerVideo{
    background-color: #2288dd;
}
</style>