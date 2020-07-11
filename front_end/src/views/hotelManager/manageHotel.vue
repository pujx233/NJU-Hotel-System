<template>
    <div class="manageHotel-wrapper">
        <a-tabs :activeKey="tabKey" @change="changeKey">
            <a-tab-pane tab="酒店管理" key="1">
                 <a-table
                    :columns="columns1"
                    :dataSource="activeHotellist"
                    bordered
                >
                    <span slot="action" slot-scope="record">
                        <a-button size="small" @click="jumpToDetails(record.id)">修改信息</a-button>
                        <a-button size="small" @click="showRoom(record)">房间管理</a-button>
                        <a-button size="small" @click="showCoupon(record)">优惠策略</a-button>
                        <a-divider type="vertical" v-if="userInfo.userType=='Administrator'"></a-divider>
                        <a-popconfirm
                            title="确定想删除该酒店吗？"
                            @confirm="deleteHotel(record)"
                            okText="确定"
                            cancelText="取消"
                        >
                            <a-button type="danger" size="small" v-if="userInfo.userType=='Administrator'">删除酒店</a-button>
                        </a-popconfirm>
                    </span>
                </a-table>
            </a-tab-pane>
            <a-tab-pane tab="订单管理" key="2">
                <a-table
                    :columns="columns2"
                    :dataSource="activeorderlist"
                    bordered
                    :locale = "filterbtn"
                >
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="roomType" slot-scope="text">
                        <span v-if="text == '大床房'">大床房</span>
                        <span v-if="text == '双床房'">双床房</span>
                        <span v-if="text == '家庭房'">家庭房</span>
                    </span>
                    <span slot="action" slot-scope="record">
                        <a-button type="primary" size="small" @click="showModal(record)">订单详情</a-button>
                        <a-button type="danger" size="small" v-if="showCheckout(record)" @click="checkout(record)">提前退房</a-button>
                        <a-button type="danger" size="small" v-if="record.orderState=='异常'" @click="deal(record)">处理异常</a-button>
                        <a-popconfirm
                                title="你确定执行该笔订单吗？"
                                @confirm="executeOrder(record.id)"
                                okText="确定"
                                cancelText="取消"
                                v-if="record.orderState == '未执行'||record.orderState=='异常'"
                        >
                            <a-button size="small">执行</a-button>
                        </a-popconfirm>
                    </span>
                </a-table>
            </a-tab-pane>
        </a-tabs>
        <Rooms></Rooms>
        <Coupon ></Coupon>
    </div>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
import AddRoomModal from './components/addRoomModal'
import Rooms from './components/Rooms'
import Coupon from './components/coupon'
const moment = require('moment')
const columns1 = [
    {  
        title: '酒店名',
        dataIndex: 'name',
    },
    {
        title: '商圈',
        dataIndex: 'bizRegion',
    },
    {
        title: '地址',
        dataIndex: 'address',
    },
    {
        title: '酒店星级',
        dataIndex: 'hotelStar'
    },
    {
        title: '评分',
        dataIndex: 'rate',
    },
    {
        title: '简介',
        dataIndex: 'description',
    },
    {
      title: '操作',
      key: 'action',
      scopedSlots: { customRender: 'action' },
    },
  ];
const columns2 = [
    {  
        title: '订单号',
        dataIndex: 'id',
    },
    {  
        title: '酒店名',
        dataIndex: 'hotelName',
    },
    {
        title: '房型',
        dataIndex: 'roomType',
        scopedSlots: { customRender: 'roomType' }
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
        title: '入住人数',
        dataIndex: 'peopleNum',
    },
    {
        title: '总价',
        dataIndex: 'price',
    },
    {
        title: '状态',
        filters: [{ text: '未执行', value: '未执行' }, { text: '已撤销', value: '已撤销' }, { text: '已执行', value: '已执行' } ,{ text: '过期异常', value: '过期异常' },{ text: '异常', value: '异常' }],
        onFilter: (value, record) => record.orderState.includes(value),
        dataIndex: 'orderState',
        scopedSlots: { customRender: 'orderState' }
    },
    {
      title: '操作',
      key: 'action',
      scopedSlots: { customRender: 'action' },
    },
  ];
export default {
    name: 'manageHotel',
    inject: ['reload'],
    data(){
        return {
            formLayout: 'horizontal',
            filterbtn: {filterConfirm: '确认',filterReset: '重置' },
            pagination: {},
            columns1,
            columns2,
            form: this.$form.createForm(this, { name: 'manageHotel' }),
            activeHotellist:[],
            activeorderlist:[],
            tabKey: '1'
        }
    },
    components: {
        Rooms,
        Coupon,
    },
    computed: {
        ...mapGetters([
            'userInfo',
            'orderList',
            'hotelList',
            'addHotelModalVisible',
            'addRoomModalVisible',
            'activeHotelId',
            'couponVisible',
            'couponList',
            'roomVisible'
        ]),
    },
    async mounted() {
        await this.getHotelList()
        var res = await this.getAllOrders()
        for(let item in this.hotelList){
            if(this.hotelList[item].managerId==this.userInfo.id){
                this.activeHotellist.push(this.hotelList[item]);
            }
        }
        for(let item in this.orderList){
            for(let item2 in this.hotelList){
                if(this.orderList[item].hotelId==this.hotelList[item2].id&&this.hotelList[item2].managerId==this.userInfo.id){
                    this.activeorderlist.push(this.orderList[item]);
                }
            }
        }
        return res
    },
    methods: {
        ...mapMutations([
            'set_addHotelModalVisible',
            'set_addRoomModalVisible',
            'set_couponVisible',
            'set_activeHotelId',
            'set_roomVisible'
        ]),
        ...mapActions([
            'getHotelList',
            'getAllOrders',
            'getHotelCoupon',
            'executeOrder',
            'getRoomList',
            'checkoutEarly',
            'solveAbnormalOrder'
        ]),
        jumpToDetails(id){
            this.$message.info('请在酒店详情页进行修改');
            this.$router.push({ name: 'hotelDetail', params: { hotelId: id }})
        },
        showModal(record) {
            const h = this.$createElement;
            if(record.haveChild==false){
                record.haveChild="无"
            }
            else{
                record.haveChild="有"
            }
            this.$info({
                title: '订单详情',
                content: h('div', {}, [
                    h('p', ''),
                    h('span','酒店名称： '),
                    h('span', record.hotelName),
                    h('p', ''),
                    h('span','入住时间： '),
                    h('span', record.checkInDate),
                    h('p', ''),
                    h('span','离开时间： '),
                    h('span', record.checkOutDate),
                    h('p', ''),
                    h('span','有无儿童： '),
                    h('span', record.haveChild),
                    h('p', ''),
                    h('span','状态： '),
                    h('span', record.orderState),
                    h('p', ''),
                    h('span','人数： '),
                    h('span', record.peopleNum),
                    h('p', ''),
                    h('span','手机号： '),
                    h('span', record.phoneNumber),
                    h('p', ''),
                    h('span','价格： '),
                    h('span', record.price),
                    h('p', ''),
                    h('span','房间数量： '),
                    h('span', record.roomNum),
                    h('p', ''),
                    h('span','房间类型： '),
                    h('span', record.roomType),
                ]),
                onOk() {},
            });
        },
        showCheckout(record){
            if(record.orderState != '已执行'){
                return false;
            }
            var now = new Date().getTime()
            if(new Date(record.checkOutDate).getTime() - now <= 0){
                return false
            }
            return true
        },
        changeKey(key){
            this.tabKey = key
        },
        checkout(record){
            var that = this
            this.$confirm({
                title: '确定要提前退房吗？',
                content: '提前退房会导致订单价格变化',
                cancelText: '取消',
                okText: '确认',
                onOk() {
                    that.checkoutEarly(record.id).then(res=>{
                        that.reload()
                    })
                },
                onCancel() {},
            });
        },
        addHotel() {
            this.set_addHotelModalVisible(true)
        },
        showRoom(record) {
            var that = this
            this.set_activeHotelId(record.id)
            this.getRoomList(this.activeHotelId).then(res=>{
                that.set_roomVisible(true)
            })
        },
        showCoupon(record) {
            this.set_activeHotelId(record.id)
            this.set_couponVisible(true)
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
        deleteHotel(){
            console.log("删除酒店")
        },
        deal(record){
            var that = this
            this.$confirm({
                title: '确定要处理该异常订单吗吗？',
                content: '处理过期较久的异常订单。这会恢复酒店相应的房间数量，但该订单无法再办理入住，客户也无法再对此异常订单进行申诉！',
                cancelText: '取消',
                okText: '确认',
                onOk() {
                    that.solveAbnormalOrder(record.id).then(res=>{
                        that.reload()
                    })
                },
                onCancel() {},
            });
        },
        renew(){
            this.mounted().then(res=>{
                this.reload()
            })
        }
    }
}
</script>
<style scoped lang="less">
    .manageHotel-wrapper {
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
    .manageHotel-wrapper {
        .ant-tabs-bar {
            padding-left: 30px
        }
    }
    .ant-btn-sm{
        margin-right: 5px;
    }
</style>
<style lang="less">
    
</style>