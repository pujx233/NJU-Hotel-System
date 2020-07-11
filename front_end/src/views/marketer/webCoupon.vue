<template>
    <div class="manageUser-wrapper">
        <a-tabs>
            <a-tab-pane tab="网站优惠列表" key="1">
                <div style="width: 100%; text-align: right; margin:20px 0">
                    <a-button type="primary" @click="addWebCoupon"><a-icon type="plus" />添加网站优惠</a-button>
                </div>
                <a-table
                        :columns="webCouponColumns"
                        :dataSource="webCouponList"
                        bordered
                >
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="action" slot-scope="text, record">
                        <a-button type="danger" @click="deleteWebCoupon(record)">删除</a-button>
                    </span>
                </a-table>
            </a-tab-pane>
        </a-tabs>
        <AddWebCouponModal :record="record" @handle-submit="renew"></AddWebCouponModal>
    </div>
</template>
<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    import AddWebCouponModal from './components/addWebCouponModal'
    const webCouponColumns = [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: '优惠券',
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
            title: '开始时间',
            dataIndex: 'startTime',
        },
        {
            title: '结束时间',
            dataIndex: 'endTime',
        },
        {
            title: '商圈',
            dataIndex: 'bizRegion',
        },
        {
            title: '操作',
            key: 'action',
            scopedSlots: { customRender: 'action' },
        },
    ];

    export default {
        name: 'addWebCoupon',
        data(){
            return {
                formLayout: 'horizontal',
                pagination: {},
                webCouponColumns,
                data: [],
                form: this.$form.createForm(this, { name: 'manageUser' }),
                record: {}
            }
        },
        components: {
            AddWebCouponModal,
        },
        inject:['reload'],
        computed: {
            ...mapGetters([
                'addWebCouponModalVisible',
                'webCouponList',
            ])
        },
        mounted() {
            this.getWebCouponList().then(res => {
                for(var i=0;i<this.webCouponList.length;i++){
                    if(this.webCouponList[i].discount==-1){
                        this.webCouponList[i].discount='无';
                    }
                    if(this.webCouponList[i].discountMoney==-1){
                        this.webCouponList[i].discountMoney='无';
                    }
                    var tmp = ''
                    switch (this.webCouponList[i].couponType) {
                        case 1:
                            tmp = 'VIP特定商圈优惠';break
                        case 2:
                            tmp = '多间特惠';break
                        case 3:
                            tmp = '满减特惠';break
                        case 4:
                            tmp = '限时优惠';
                            this.webCouponList[i].startTime = this.webCouponList[i].startTime.replace('T',' ');
                            this.webCouponList[i].endTime = this.webCouponList[i].endTime.replace('T',' ');
                            break
                    }
                    this.webCouponList[i].couponType = tmp
                    tmp = ''
                    switch (this.webCouponList[i].bizRegion) {
                        case 'XiDan':
                            tmp = '西单';break
                        case 'WangFuJing':
                            tmp = '王府井';break
                        case 'DaZhaLan':
                            tmp = '大栅栏';break
                    }
                    this.webCouponList[i].bizRegion = tmp
                }
            })
        },
        methods: {
            ...mapActions([
                'getWebCouponList',
                'delWebCoupon',
            ]),
            ...mapMutations([
                'set_addWebCouponModalVisible',
            ]),
            addWebCoupon(record){
                this.record = record
                this.set_addWebCouponModalVisible(true)
            },
            deleteWebCoupon(record){
                var that = this
                this.$confirm({
                    title: '确定要删除该优惠券吗？',
                    content: '删除后无法恢复噢',
                    cancelText: '取消',
                    okText: '确认',
                    onOk() {
                        that.delWebCoupon(record.id).then(res=>{
                            that.reload()
                        })
                    },
                    onCancel() {},
                });
            },
            renew(){
                this.getWebCouponList().then(res=>{
                    this.reload()
                })
            }
        }
    }
</script>
<style scoped lang="less">
    .manageUser-wrapper {
        padding: 50px;
        .chart {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-top: 20px
        }
    }
</style>
<style lang="less">
    .manageUser-wrapper {
        .ant-tabs-bar {
            padding-left: 30px
        }
    }
    .ant-btn-danger{
        margin-right: 10px;
    }
</style>
<style lang="less">

</style>