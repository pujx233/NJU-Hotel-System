<template>
    <div class="info-wrapper">
        <a-tabs>
            <a-tab-pane tab="我的信息" key="1">
                <a-form :form="form" style="margin-top: 30px" v-if="birthday==false">
                    <a-form-item label="用户名" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1  }">
                        <a-input
                            placeholder="请填写用户名"
                            v-decorator="['userName', { rules: [{ required: false, message: '请输入用户名' }] }]"
                            v-if="modify"
                    />
                        <span v-else>{{ userInfo.userName }}</span>
                    </a-form-item>
                    <a-form-item label="邮箱" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                        <span>{{ userInfo.email }}</span>
                    </a-form-item>
                    
                    <a-form-item label="手机号" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                        <a-input
                            placeholder="请填写手机号"
                            v-decorator="['phoneNumber', { rules: [{ required: false, message: '请输入手机号' }] }]"
                            v-if="modify"
                        />
                        <span v-else>{{ userInfo.phoneNumber}}</span>
                    </a-form-item>
                    <a-form-item label="信用值" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }" v-if="userInfo.userType === 'Client'">
                        <span>{{ userInfo.credit }}</span>
                    </a-form-item>
                    <a-form-item label="VIP等级" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }" v-if="userInfo.userType === 'Client'">
                        <span>{{ VIPInfo.level }}</span>
                    </a-form-item>
                    <a-form-item label="密码" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }" v-if="modify">
                        <a-input
                            placeholder="请输入新密码"
                            v-decorator="['password', { rules: [{ required: false, message: '请输入新密码' }] }]"
                        />
                    </a-form-item>
                    <a-form-item :wrapper-col="{ span: 12, offset: 5 }" v-if="modify">
                        <a-button type="primary" @click="saveModify">
                            保存
                        </a-button>
                        <a-button type="default" style="margin-left: 30px" @click="cancelModify">
                            取消
                        </a-button>
                    </a-form-item>
                     <a-form-item :wrapper-col="{ span: 8, offset: 4 }" v-else>
                        <a-button type="primary" @click="modifyInfo">
                            修改信息和密码
                        </a-button>
                         <a-divider type="vertical"></a-divider>
                             <a-button type="primary" @click="birthdayInfo" v-if="userInfo.userType==='Client'&&VIPInfo.level=='非会员'">
                             注册会员
                             </a-button>
                    </a-form-item>
                </a-form>
                <a-form :form="form" style="margin-top: 30px" v-else>
                    <a-form-item label="生日日期" v-if="birthday">
                       <a-date-picker
                                format="YYYY-MM-DD"
                                v-decorator="['date']"
                                v-if="birthday"
                        />
                    </a-form-item>

                    <a-form-item :wrapper-col="{ span: 12, offset: 5 }" v-if="birthday">
                        <a-button type="primary" @click="savebirthday">
                            注册
                        </a-button>
                        <a-button type="default" style="margin-left: 30px" @click="cancelbirthday">
                            取消
                        </a-button>
                    </a-form-item>
                </a-form>
            </a-tab-pane>
            <a-tab-pane tab="我的订单" key="2" v-if="userInfo.userType==='Client'">
                <a-table
                    :columns="columns"
                    :dataSource="userOrderList"
                    bordered
                >
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="roomType" slot-scope="text">
                        <span v-if="text == '大床房'">大床房</span>
                        <span v-if="text == '双床房'">双床房</span>
                        <span v-if="text == '家庭房'">家庭房</span>
                    </span>
                    <a-tag slot="orderState" color="blue" slot-scope="text">
                        {{ text }}
                    </a-tag>
                    <span slot="action" slot-scope="record">
                        <a-button type="primary" size="small"  @click="showModal(record)">查看</a-button>
                        <a-divider type="vertical" v-if="record.orderState == '未执行'"></a-divider>
                        <a-popconfirm
                                title="你确定撤销该笔订单吗？"
                                @confirm="confirmCancelOrder(record.id)"
                                @cancel="cancelCancelOrder"
                                okText="确定"
                                cancelText="取消"
                                v-if="record.orderState == '未执行'"
                        >
                            <a-button type="danger" size="small">撤销</a-button>
                        </a-popconfirm>
                         <a-divider type="vertical" v-if="record.orderState == '已执行'"></a-divider>
                            <a-button size="small" @click="showEvaluate(record)" v-if="record.orderState == '已执行'">评价</a-button>

                    </span>
                </a-table>
            </a-tab-pane>
        </a-tabs>
        <evaluate></evaluate>
    </div>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
import Evaluate from "./components/evauate";
const moment = require('moment')
const columns = [
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
        title: '房价',
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
    name: 'info',
    data(){
        return {
            nowTime:'',
            confirmLoading:false,
            birthday:false,
            modify: false,
            formLayout: 'horizontal',
            pagination: {},
            columns,
            data: [],
            form: this.$form.createForm(this, { name: 'coordinated' }),
            record:{},
        }
    },
    components: {
        Evaluate
    },
    computed: {
        ...mapGetters([
            'userId',
            'userInfo',
            'userOrderList',
            'evaluateVisible',
            'VIPInfo'
        ])
    },
    async mounted() {
        await this.getUserInfo()
        await this.getUserOrders()
        this.getVIP()
        console.log(this.VIPInfo)
    },
    methods: {
        ...mapMutations([
            'set_evaluateVisible',
            'set_currentOrderId'
        ]),
        ...mapActions([
            'getUserInfo',
            'getUserOrders',
            'updateUserInfo',
            'cancelOrder',
            'registerVIP',
            'getVIP'
        ]),
        timeFormate(timeStamp) {
            let year = new Date(timeStamp).getFullYear();
            let month =new Date(timeStamp).getMonth() + 1 < 10? "0" + (new Date(timeStamp).getMonth() + 1): new Date(timeStamp).getMonth() + 1;
            let date =new Date(timeStamp).getDate() < 10? "0" + new Date(timeStamp).getDate(): new Date(timeStamp).getDate();
            this.nowTime = year + "-" + month + "-" + date ;
        },
        nowTimes() {
            this.timeFormate(new Date());
        },
        compareDate (d1, d2) {
            let reg = new RegExp('-', 'g')
            return ((new Date(d1.replace(reg, '/'))) > (new Date(d2.replace(reg, '/'))))
        },
        showEvaluate(record) {
            this.nowTimes()
            console.log()
            if(this.compareDate(record.checkOutDate,this.nowTime)){
                this.$message.info('请离店后评价哦！');
            }else{
                if(record.rar==true){
                    this.$message.info('您已经评价过！');
                }
                else{
                    this.set_currentOrderId(record.id)
                    for(let item in this.userOrderList){
                        if(this.userOrderList[item].id==record.id){
                            this.userOrderList[item].rar=true
                        }
                    }
                    this.set_evaluateVisible(true)
                }
            }
        },
        handleCancel(e) {
            console.log('Clicked cancel button');
            this.visible = false;
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
        saveModify() {
            this.form.validateFields((err, values) => {
                if (this.form.getFieldValue('password')!=null) {
                    const data = {
                        userName: this.form.getFieldValue('userName'),
                        phoneNumber: this.form.getFieldValue('phoneNumber'),
                        password: this.form.getFieldValue('password')
                    }
                    this.updateUserInfo(data).then(()=>{
                        this.modify = false
                    })
                }
                else{
                    const data = {
                        userName: this.form.getFieldValue('userName'),
                        phoneNumber: this.form.getFieldValue('phoneNumber'),
                        password: this.userInfo.password,
                    }
                    this.updateUserInfo(data).then(()=>{
                        this.modify = false
                    })
                }
            });
        },
        savebirthday() {
            const data= {
                userId:this.userId,
                birthday: moment(this.form.getFieldValue('date')).format('YYYY-MM-DD')+'T00:00:00'
            }
            this.registerVIP(data)
            this.birthday=false
            this.VIPInfo.level=1
            this.getVIP()
        },
        modifyInfo() {
            setTimeout(() => {
                this.form.setFieldsValue({
                    'userName': this.userInfo.userName,
                    'phoneNumber': this.userInfo.phoneNumber,
                })
            }, 0)
            this.modify = true
        },
       birthdayInfo() {
            this.birthday = true
        },
        cancelModify() {
            this.modify = false
        },
        cancelbirthday() {
            this.birthday = false
        },
        confirmCancelOrder(orderId){
            this.cancelOrder(orderId)
        },
        cancelCancelOrder() {

        }
        
    }
}
</script>
<style scoped lang="less">
    .info-wrapper {
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
    .info-wrapper {
        .ant-tabs-bar {
            padding-left: 30px
        }
    }
</style>
<style lang="less">
    
</style>