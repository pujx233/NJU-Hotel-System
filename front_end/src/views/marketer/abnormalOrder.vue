<template>
    <div class="addHotel-wrapper">
        <a-tabs>
            <a-tab-pane tab="异常订单" key="1">
                <a-table
                        :columns="columns"
                        :dataSource="abnormalOrderList"
                        bordered
                >
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="action" slot-scope="text, record">
                        <a-button type="primary" @click="annul(record)">撤销</a-button>
                    </span>
                </a-table>
            </a-tab-pane>
        </a-tabs>
        <cancelAbOrderModal :record="record" @handle-submit="renew"></cancelAbOrderModal>
    </div>
</template>

<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    import cancelAbOrderModal from './components/cancelAbOrderModal'
    const columns = [
        {
            title: '订单ID',
            dataIndex: 'id',
        },
        {
            title: '客户邮箱',
            dataIndex: 'clientEmail',
        },
        {
            title: '酒店名',
            dataIndex: 'hotelName',
        },
        {
            title: '入住时间',
            dataIndex: 'checkInDate',
            scopedSlots: { customRender: 'checkInDate' }
        },
        {
            title: '离店时间',
            dataIndex: 'checkOutDate',
            scopedSlots: { customRender: 'checkOutDate' }
        },
        {
            title: '房价',
            dataIndex: 'price',
        },
        {
            title: '操作',
            key: 'action',
            scopedSlots: { customRender: 'action' },
        },
    ];

    export default {
        name: "abnormalOrder",
        inject:['reload'],
        data(){
            return {
                formLayout: 'horizontal',
                pagination: {},
                columns,
                data: [],
                form: this.$form.createForm(this, { name: 'addHotel' }),
                record:{}
            }
        },
        components: {
            cancelAbOrderModal
        },
        computed: {
            ...mapGetters([
                'cancelAbOrderModalVisible',
                'abnormalOrderList'
            ])
        },
        mounted() {
            this.getabnormalOrderList()
        },
        methods: {
            ...mapActions([
                'getabnormalOrderList'
            ]),
            ...mapMutations([
                'set_cancelAbOrderModalVisible'
            ]),
            annul(record){
                this.record=record
                this.set_cancelAbOrderModalVisible(true)
            },
            renew(){
                this.getabnormalOrderList().then(res=>{
                    this.reload()
                })
            }
        }
    }
</script>

<style scoped lang="less">
    .addHotel-wrapper {
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
    .addHotel-wrapper {
        .ant-tabs-bar {
            padding-left: 30px
        }
    }
</style>
<style lang="less">

</style>