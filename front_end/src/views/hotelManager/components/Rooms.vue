<template>
    <div>
        <a-modal
                :visible="roomVisible"
                title="房间管理"
                width="1100px"
                :footer="null"
                @cancel="cancel"
        >
            <div style="width: 100%; text-align: right; margin:20px 0">
                <a-button type="primary" @click="addRoom">
                    <a-icon type="plus"/>
                    添加房型
                </a-button>
            </div>
            <a-table :columns="columns" :dataSource="roomList" bordered>
                <a-tag color="red" slot="couponName" slot-scope="text"> {{text}}</a-tag>
                <span slot="action" slot-scope="text, record">
                    <a-button type="primary" @click="amend(record)">修改</a-button>
                    <a-button type="danger" @click="deleteRoom(record)">删除</a-button>
                    <a-button style="margin-left: 5px"  @click="amendRoomNum(record)">修改剩余房数</a-button>
                </span>
            </a-table>

        </a-modal>
        <addRoomModal :record="record" @handle-submit="renew"></addRoomModal>
        <amendRoomModal :record="record" @handle-submit="renew"></amendRoomModal>
        <amendRoomNum :record="record" @handle-submit="renew"></amendRoomNum>
    </div>
</template>
<script>
    import {mapGetters, mapMutations, mapActions} from 'vuex'
    import addRoomModal from './addRoomModal'
    import amendRoomModal from './amendRoomModal'
    import amendRoomNum from './amendRoomNum'
    const columns = [
        {
            title: '房型',
            dataIndex: 'roomType',
        },
        {
            title: '价格',
            dataIndex: 'price',
        },
        {
            title: '总数',
            dataIndex: 'total',
        },
        {
            title: '剩余房数',
            dataIndex: 'curNum',
        },

        {
            title: '操作',
            key: 'action',
            scopedSlots: { customRender: 'action' },
        },
    ];
    /* 作业修改*/
    export default {
        name: 'Rooms',
        inject:['reload'],
        data() {
            return {
                columns,
                record: {}
            }
        },
        components: {
            amendRoomModal,
            addRoomModal,
            amendRoomNum,
        },
        computed: {
            ...mapGetters([
                'roomVisible',
                'roomList',
                'amendRoomModalVisible',
                'activeHotelId',
            ])
        },
        mounted() {

        },
        methods: {
            ...mapMutations([
                'set_addRoomModalVisible',
                'set_roomVisible',
                'set_roomList',
                'set_amendRoomModalVisible',
                'set_amendRoomNumVisible'
            ]),
            ...mapActions([
                'getHotelById',
                'delRoom',
                'getRoomList',
                'getHotelCoupon'
            ]),
            cancel() {
                this.set_roomVisible(false)
            },
            addRoom() {
                this.set_addRoomModalVisible(true)
                this.set_roomVisible(false)
            },
            amend(record){
                this.record=record
                this.set_roomVisible(false)
                this.set_amendRoomModalVisible(true)
            },
            amendRoomNum(record){
                this.record=record
                this.set_amendRoomNumVisible(true)
            },
            deleteRoom(record){
                this.record = record
                const h = this.$createElement;
                var that = this
                this.$confirm({
                    title: '确定删除该房间吗?',
                    content: h('div', {}, [
                        h('p', ''),
                        h('span','房间类型： '),
                        h('span', record.roomType),
                    ]),
                    cancelText: '取消',
                    okText: '确定',
                    onOk() {
                        const data = {
                            roomType: record.roomType,
                            hotelId: record.id,
                        }
                        console.log("删除房间",data)
                        that.delRoom(data).then(res=>{
                            that.renew()
                        })
                    },
                    onCancel() {},
                });

            },
            renew(){
                this.getHotelCoupon().then(res=>{
                    this.getRoomList(this.activeHotelId)
                }).then(res=>{
                    this.reload()
                })
            }
        },
    }
</script>
<style scoped>
    .ant-btn-danger{
        margin-left: 10px;
    }
</style>