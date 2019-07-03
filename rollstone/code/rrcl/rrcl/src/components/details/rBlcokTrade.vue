<!--监控页面的块信息及交易信息-->
<template>
    <div class="blockTrade">
        <Breadcrumb>
            <BreadcrumbItem to="/ChainDetails">区块/交易信息</BreadcrumbItem>
            <BreadcrumbItem>区块/交易详情</BreadcrumbItem>
        </Breadcrumb>
        <div class="blocksDetails">
            <Row>
                <Col span="6">区块挖矿难度：<em>{{difficulty}}</em></Col>
                <Col span="6">全链挖矿难度：<em>{{totalDifficulty}}</em></Col>
                <Col span="6">区块Gas消耗上线：<em>{{gasLimit}}</em></Col>
                <Col span="6">区块实际消耗Gas：<em>{{gasUsed}}</em></Col>
            </Row>
            <Row>
                <Col span="6">区块编号：<em>{{number}}</em></Col>
                <Col span="6">区块大小：<em>{{size}}</em></Col>
                <Col span="6">区块生成节点：<em>{{signer}}</em></Col>
                <Col span="6">生成时间：<em>{{timestamp}}</em></Col>
            </Row>
            <Row>
                <Col span="12">区块哈希：<em>{{hash}}</em></Col>
                <Col span="12">父区块哈希：<em>{{parentHash}}</em></Col>
            </Row>
            <Row>
            </Row>
            <Row>
                <Col span="12">凭证树根哈希:<em>{{receiptsRoot}}</em></Col>
                <Col span="12">叔块哈希集合：<em>{{sha3Uncles}}</em></Col>
            </Row>
            <Row>
                <Col span="12">状态树根哈希：<em>{{stateRoot}}</em></Col>
            </Row>
            <Row>
            </Row>
        </div>
        <div class="tradeDetails">
            <Table :loading="loading" :columns="columns" :data="data"></Table>
        </div>
    </div>
</template>
<script>
import Vue from 'vue'
import VueSocketio from 'vue-socket.io';
import socketio from 'socket.io-client';

Vue.use(VueSocketio, socketio('http://172.16.201.189:8000/mio'));
export default {
    data(){
        return{
            loading:true,
            blockHash:'',
            difficulty:'',
            gasLimit:'',
            gasUsed:'',
            hash:'',
            number:'',
            parentHash:'',
            receiptsRoot:'',
            sha3Uncles:'',
            signer:'',
            size:'',
            stateRoot:'',
            timestamp:'',
            totalDifficulty:'',
            columns: [
                {
                    title: '交易哈希',
                    key: 'hash'
                },
                {
                    title: '交易发起者',
                    key: 'from',
                },
                {
                    title: '交易受益者',
                    key: 'to'
                },
                {
                    title: '交易额',
                    key: 'value'
                },
                {
                    title: '交易状态',
                    key: 'status',
                    width:100
                },
                {
                    title: '操作',
                    key: 'action',
                    width: 150,
                    align: 'center',
                    render: (h, params) => {
                        return h('div', [
                            h('Button', {
                                props: {
                                    type: 'primary',
                                    size: 'small'
                                },
                                style: {
                                    marginRight: '5px'
                                },
                                on: {
                                    click: () => {
                                        this.show(params.index)
                                    }
                                }
                            }, '更多')
                        ]);
                    }
                }
            ],
            data: []
        }

    },
    sockets:{ //将（socket.on）绑定事件放在sockets中
        connect: function(){
            console.log('socket connected')
        },
        connect_error:function(error){
                this.$router.push({path:'/Error'})
                this.$socket.disconnect();
            },
        get_block(val){
            this.difficulty=val.msg.block.difficulty
            this.gasLimit=val.msg.block.gasLimit
            this.gasUsed=val.msg.block.gasUsed
            this.difgasUsedficulty=val.msg.block.difgasUsedficulty
            this.hash=val.msg.block.hash
            this.number=val.msg.block.number
            this.parentHash=val.msg.block.parentHash
            this.receiptsRoot=val.msg.block.receiptsRoot
            this.sha3Uncles=val.msg.block.sha3Uncles
            this.signer=val.msg.block.signer
            this.size=val.msg.block.size
            this.stateRoot=val.msg.block.stateRoot
            this.timestamp=new Date(val.msg.block.timestamp*1000).toLocaleString()
            this.totalDifficulty=val.msg.block.totalDifficulty;
            for(var i in val.msg.txs){
                if(val.msg.txs[i].status===true){
                    val.msg.txs[i].status="正常"
                }else if(val.msg.txs[i].status===false){
                    val.msg.txs[i].status="异常"
                }
            };
            this.data = val.msg.txs
            this.loading = false;
        }
    },
    mounted(){
        },
    watch: {
        '$route' (to, from) {
            // 对路由变化作出响应...
            this.blockHash = this.$route.query.hash
            this.getBlocksTrade()
        }
    },
    created(){
        this.blockHash = this.$route.query.hash
        this.getBlocksTrade()
    },
    methods:{
        //获取区块详情及交易
        getBlocksTrade(){
            var blkhash ={
                blkhash:this.blockHash
            } 
            this.$socket.emit('getBlock', blkhash);
        },
        show (index) {
                this.$Modal.info({
                    title: '交易详情',
                    content: `区块编号：${this.data[index].blockNumber}<br>
                              交易序号：${this.data[index].nonce}<br>
                              交易位置：${this.data[index].transactionIndex}<br>
                              合约地址：${this.data[index].contractAddress}<br>
                              交易消耗的Gas：${this.data[index].gas}<br>
                              当前Gas价格：${this.data[index].gasPrice}<br>`
                })
            }
    },

}
</script>
<style scoped>
.blocksDetails{
    line-height: 30px;
    padding: 10px 0 20px 0;
    color: #2d8cf0;
}
.blocksDetails em{
    color: #b14b10;
}
.ivu-breadcrumb {
    color: #2d8cf0;
    font-size: 14px;
    margin-top: 20px;
}
.ivu-breadcrumb a{
    color: #2d8cf0;
}

</style>
