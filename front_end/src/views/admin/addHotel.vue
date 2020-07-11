<template>
    <div class="addHotel-wrapper">
        <a-tabs>
            <a-tab-pane tab="添加酒店" key="2">
                <div style="width: 100%; text-align: right; margin:20px 0">
                    <a-button type="primary" @click="addHotel"><a-icon type="plus" />添加酒店</a-button>
                </div>
                <a-table
                        :columns="columns"
                        :dataSource="hotelList"
                        bordered
                >
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="action" slot-scope="text, record">
                        <a-button type="danger" @click="showConfirm(record)">删除酒店</a-button>
                    </span>
                </a-table>
            </a-tab-pane>
        </a-tabs>
        <AddHotelModal></AddHotelModal>
    </div>
</template>

<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    import AddHotelModal from './components/addHotelModal'
    import AddManagerModal from "./components/addManagerModal";
    const columns = [
        {
            title: '酒店ID',
            dataIndex: 'id',
        },
        {
            title: '酒店名',
            dataIndex: 'name',
        },
        {
            title: '酒店电话',
            dataIndex: 'phoneNum',
        },
        {
            title: '工作人员ID',
            dataIndex: 'managerId',
        },
        {
            title: '操作',
            key: 'action',
            scopedSlots: { customRender: 'action' },
        },
    ];

    export default {
        name: "addHotel",
        data(){
            return {
                formLayout: 'horizontal',
                pagination: {},
                columns,
                data: [],
                form: this.$form.createForm(this, { name: 'addHotel' }),
            }
        },
        components: {
            AddHotelModal
        },
        computed: {
            ...mapGetters([
                'addHotelModalVisible',
                'hotelList',
                'userId',
            ])
        },
        mounted() {
            this.getHotelList()
        },
        methods: {
            ...mapActions([
                'getHotelList',
                'deleteHotel'
            ]),
            ...mapMutations([
                'set_addHotelModalVisible'
            ]),
            addHotel(){
                this.set_addHotelModalVisible(true)
            },
            showConfirm(record){
                const h = this.$createElement;
                var that = this
                const data = {hotelId:record.id,adminId: this.userId}
                this.$confirm({
                    title: '确定删除该酒店吗?',
                    content: h('div', {}, [
                        h('p', ''),
                        h('span','酒店ID： '),
                        h('span', record.id),
                        h('p', ''),
                        h('span','酒店名称： '),
                        h('span', record.name),
                    ]),
                    cancelText: '取消',
                    okText: '确定',
                    onOk() {
                        that.deleteHotel(data)
                    },
                    onCancel() {},
                });
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