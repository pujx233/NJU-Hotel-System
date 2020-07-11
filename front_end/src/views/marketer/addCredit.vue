<template>
    <div class="manageUser-wrapper">
        <a-tabs>
            <a-tab-pane tab="客户列表" key="1">
                <div style="width: 100%; text-align: right; margin:20px 0">
                    <a-button type="primary" @click="addCredit"><a-icon type="plus" />快速查找</a-button>
                </div>
                <a-table
                        :columns="userColumns"
                        :dataSource="clientList"
                        bordered
                >
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="action" slot-scope="text, record">
                        <a-button type="primary" @click="addCredit(record)">修改信用值</a-button>
                    </span>
                </a-table>
            </a-tab-pane>
        </a-tabs>
        <AddCreditModal :record="record" @handle-submit="renew"></AddCreditModal>
    </div>
</template>
<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    import AddCreditModal from './components/addCreditModal'
    const userColumns = [
        {
            title: 'ID',
            dataIndex: 'id',
        },
        {
            title: '用户邮箱',
            dataIndex: 'email',
        },
        {
            title: '用户名',
            dataIndex: 'userName',
        },
        {
            title: '用户手机号',
            dataIndex: 'phoneNumber',
        },
        {
            title: '信用值',
            dataIndex: 'credit',
        },
        {
            title: '操作',
            key: 'action',
            scopedSlots: { customRender: 'action' },
        },
    ];

    export default {
        name: 'addCredit',
        data(){
            return {
                formLayout: 'horizontal',
                pagination: {},
                userColumns,
                data: [],
                form: this.$form.createForm(this, { name: 'manageUser' }),
                record: {}
            }
        },
        components: {
            AddCreditModal,
        },
        inject:['reload'],
        computed: {
            ...mapGetters([
                'addCreditModalVisible',
                'clientList',
            ])
        },
        mounted() {
            this.getClientList()
        },
        methods: {
            ...mapActions([
                'getClientList',
            ]),
            ...mapMutations([
                'set_addCreditModalVisible',
            ]),
            addCredit(record){
                this.record = record
                this.set_addCreditModalVisible(true)
            },
            renew(userType){
                this.getClientList().then(res=>{
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