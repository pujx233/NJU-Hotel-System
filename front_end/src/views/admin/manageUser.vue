<template>
    <div class="manageUser-wrapper">
        <a-tabs>
            <a-tab-pane tab="客户" key="1">
                <a-table
                    :columns="userColumns"
                    :dataSource="clientList"
                    bordered
                >
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="action" slot-scope="text, record">
<!--                        <a-button type="danger" @click="del(record)">删除账号</a-button>-->
                        <a-button type="primary" @click="amend(record)">修改信息</a-button>
                    </span>
                </a-table>
            </a-tab-pane>
            <a-tab-pane tab="酒店工作人员" key="2">
                <div style="width: 100%; text-align: right; margin:20px 0">
                    <a-button type="primary" @click="addManager"><a-icon type="plus" />添加酒店工作人员</a-button>
                </div>
                <a-table
                        :columns="HMColumns"
                        :dataSource="managerList"
                        bordered
                >
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="action" slot-scope="text, record">
                        <a-button type="danger" @click="showConfirm(record)">删除账号</a-button>
                        <a-button type="primary" @click="amend(record)">修改信息</a-button>
                    </span>
                </a-table>
            </a-tab-pane>
            <a-tab-pane tab="网站营销人员" key="3">
                <div style="width: 100%; text-align: right; margin:20px 0">
                    <a-button type="primary" @click="addMarketer"><a-icon type="plus" />添加网站营销人员</a-button>
                </div>
                <a-table
                        :columns="MarketerColumns"
                        :dataSource="marketerList"
                        bordered
                >
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="action" slot-scope="text, record">
                        <a-button type="danger" @click="showConfirm(record)">删除账号</a-button>
                        <a-button type="primary" @click="amend(record)">修改信息</a-button>
                    </span>
                </a-table>
            </a-tab-pane>
        </a-tabs>
        <AddManagerModal></AddManagerModal>
        <AddMarketerModal></AddMarketerModal>
        <AmendUserModal :record="record" @handle-submit="renew"></AmendUserModal>
    </div>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
import AddManagerModal from './components/addManagerModal'
import AddMarketerModal from './components/addMarketerModal'
import AmendUserModal from './components/amendUserModal'
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
        title: '用户密码',
        dataIndex: 'password',
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
const HMColumns = [
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
        title: '用户密码',
        dataIndex: 'password',
    },
    {
        title: '用户手机号',
        dataIndex: 'phoneNumber',
    },
    {
        title: '操作',
        key: 'action',
        scopedSlots: { customRender: 'action' },
    },
];
const MarketerColumns = [
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
        title: '用户密码',
        dataIndex: 'password',
    },
    {
        title: '用户手机号',
        dataIndex: 'phoneNumber',
    },
    {
        title: '操作',
        key: 'action',
        scopedSlots: { customRender: 'action' },
    },
];
export default {
    //name: 'manageHotel',
    name: 'manageUser',
    data(){
        return {
            formLayout: 'horizontal',
            pagination: {},
            userColumns,
            HMColumns,
            MarketerColumns,
            data: [],
            form: this.$form.createForm(this, { name: 'manageUser' }),
            record: {}
        }
    },
    components: {
        AddManagerModal,
        AddMarketerModal,
        AmendUserModal
    },
    inject:['reload'],
    computed: {
        ...mapGetters([
            'addManagerModalVisible',
            'addMarketerModalVisible',
            'amendUserModalVisible',
            'managerList',
            'clientList',
            'marketerList'
        ])
    },
    mounted() {
        this.getManagerList()
        this.getClientList()
        this.getMarketerList()
    },
    methods: {
        ...mapActions([
            'getManagerList',
            'getClientList',
            'getMarketerList',
            'removeUser'
        ]),
        ...mapMutations([
            'set_addManagerModalVisible',
            'set_addMarketerModalVisible',
            'set_amendUserModalVisible'
        ]),
        addManager(){
            this.set_addManagerModalVisible(true)
        },
        addMarketer(){
            this.set_addMarketerModalVisible(true)
        },
        amend(record){
            this.record = record
            this.set_amendUserModalVisible(true)
        },
        showConfirm(record){
            var that = this
            const h = this.$createElement;
            this.$confirm({
                title: '确定删除该用户吗?',
                content: h('div', {}, [
                    h('p', ''),
                    h('span','邮箱： '),
                    h('span', record.email),
                    h('p', ''),
                    h('span','用户名： '),
                    h('span', record.userName),
                ]),
                cancelText: '取消',
                okText: '确定',
                onOk() {
                    that.removeUser(record)
                },
                onCancel() {},
            });
        },
        renew(userType){
            if(userType==='Client'){
                this.getClientList().then(res=>{
                    this.reload()
                })
            }else if(userType==='HotelManager'){
                this.getManagerList().then(res=>{
                    this.reload()
                })
            }else if(userType==='Marketer'){
                this.getMarketerList().then(res=>{
                    this.reload()
                })
            }

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