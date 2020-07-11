<template>
    <div>
        <a-modal
                :visible="couponVisible"
                title="优惠策略"
                width="1100px"
                :footer="null"
                @cancel="cancel"
        >
            <div style="width: 100%; text-align: right; margin:20px 0">
                <a-button type="primary" @click="addCoupon">
                    <a-icon type="plus"/>
                    添加优惠策略
                </a-button>
            </div>
            <a-table :columns="columns" :dataSource="couponList" bordered>
                <a-tag color="red" slot="couponName" slot-scope="text"> {{text}}</a-tag>
                <span slot="action" slot-scope="text, record">
                        <a-button type="danger" @click="deleteWebCoupon(record)">删除</a-button>
                </span>
            </a-table>

        </a-modal>
        <AddCoupon @handle-submit="renew"></AddCoupon>
    </div>
</template>
<script>
    import {mapGetters, mapMutations, mapActions} from 'vuex'
    import AddCoupon from './addCoupon'

    /* 作业修改 by曾昭宁*/
    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: '优惠名',
            dataIndex: 'couponName',
        },
        {
            title: '类型',
            dataIndex: 'couponType',
        },
        {
            title: '折扣',
            dataIndex: 'discount',
        },
        {
            title: '直减金额',
            dataIndex: 'discountMoney',
        },
        {
            title: '达标金额',
            dataIndex: 'targetMoney',
        },
        {
            title: '达标房数',
            dataIndex: 'roomNum',
        },
        {
            title: '开始时间',
            dataIndex: 'startTime',
        },
        {
            title: '结束时间',
            dataIndex: 'endTime',
        },
        {
            title: '操作',
            key: 'action',
            scopedSlots: { customRender: 'action' },
        },
    ];
    /* 作业修改*/
    export default {
        name: 'coupon',
        inject:['reload'],
        data() {
            return {
                columns,
            }
        },
        components: {
            AddCoupon,
        },
        computed: {
            ...mapGetters([
                'couponVisible',
                'couponList',
            ])
        },
        mounted() {
            this.getHotelCoupon().then(res=>{
                for(var i=0;i<this.couponList.length;i++){
                    if(this.couponList[i].discount==-1){
                        this.couponList[i].discount=0;
                    }
                    if(this.couponList[i].discountMoney==-1){
                        this.couponList[i].discountMoney=0;
                    }
                    var tmp = ''
                    switch (this.couponList[i].couponType) {
                        case 1:
                            tmp = 'VIP特定商圈优惠';break
                        case 2:
                            tmp = '多间特惠';break
                        case 3:
                            tmp = '满减特惠';break
                        case 4:
                            tmp = '限时优惠';
                            this.couponList[i].startTime = this.couponList[i].startTime.replace('T',' ');
                            this.couponList[i].endTime = this.couponList[i].endTime.replace('T',' ');
                            break
                    }
                    this.couponList[i].couponType = tmp
                    tmp = ''
                    switch (this.couponList[i].bizRegion) {
                        case 'XiDan':
                            tmp = '西单';break
                        case 'WangFuJing':
                            tmp = '王府井';break
                        case 'DaZhaLan':
                            tmp = '大栅栏';break
                    }
                    this.couponList[i].bizRegion = tmp
                }
            })
        },
        methods: {
            ...mapMutations([
                'set_addCouponVisible',
                'set_couponVisible',
            ]),
            ...mapActions([
                'getHotelCoupon',
                'delWebCoupon'
            ]),
            cancel() {
                this.set_couponVisible(false)
            },
            addCoupon() {
                this.set_addCouponVisible(true),
                this.set_couponVisible(false)
            },
            deleteWebCoupon(record){
                const h = this.$createElement;
                var that = this
                this.$confirm({
                    title: '确定删除该优惠吗?',
                    content: h('div', {}, [
                        h('p', ''),
                        h('span','id： '),
                        h('span', record.id),
                        h('p', ''),
                        h('span','优惠名： '),
                        h('span', record.couponName),
                        h('p', ''),
                        h('span','优惠简介： '),
                        h('span', record.description),
                    ]),
                    cancelText: '取消',
                    okText: '确定',
                    onOk() {
                        that.delWebCoupon(record.id).then(res=>{
                            that.renew()
                        })
                    },
                    onCancel() {},
                });

            },
            renew(){
                this.getHotelCoupon().then(res=>{
                    this.reload()
                })
            }
        },
    }
</script>
<style scoped>
</style>