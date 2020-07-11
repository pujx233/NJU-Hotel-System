<template>
    <a-modal
        :visible="addCouponVisible"
        title="添加优惠策略"
        cancelText="取消"
        okText="确定"
        @cancel="cancel"
        @ok="handleSubmit"
    >
        <!-- 这里是添加策略模态框区域，请编写表单 -->
        <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
            <a-form-item label="优惠券类型" v-bind="formItemLayout">
                <a-select
                        v-decorator="[
                    'type',
                    { rules: [{ required: true, message: '请选择优惠券类型' }] }]"
                        @change="changeType"
                >
                    <a-select-option value="1">VIP生日特惠</a-select-option>
                    <a-select-option value="2">多间特惠</a-select-option>
                    <a-select-option value="3">满减特惠</a-select-option>
                    <a-select-option value="4">限时优惠</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="券名">
                <a-input
                        placeholder="请填写券名"
                        v-decorator="['name', { rules: [{ required: true, message: '请填写券名' }] }]"
                />
            </a-form-item>
            <a-form-item label="优惠简介" v-bind="formItemLayout">
                <a-input
                        type="textarea"
                        :rows="4"
                        placeholder="请填写优惠简介"
                        v-decorator="['description', { rules: [{ required: true, message: '请填写优惠简介' }] }]"
                />
            </a-form-item>
            <a-form-item label="达标金额" v-if="strategy == 3" v-bind="formItemLayout">
                <a-input
                        placeholder="请填写达标金额"
                        v-decorator="['targetMoney', { rules: [{ required: true , message: '请填写达标金额' }] }]"
                />
            </a-form-item>
            <a-form-item label="达标房间数量" v-if="strategy == 2" v-bind="formItemLayout">
                <a-input
                        placeholder="请填写达标房间数量"
                        v-decorator="['roomNum', { rules: [{ required: true, message: '请填写达标金额' }] }]"
                />
            </a-form-item>
            <a-form-item v-bind="formItemLayout" label="优惠期间" v-if="strategy == 4">
                <a-range-picker
                        format="YYYY-MM-DD HH:mm:ss"
                        v-decorator="[
                        'time',
                        {
                            rules: [{ required: true, message: '请选择优惠时间' }]
                        }
                    ]"
                        :placeholder="['开始日期','结束日期']"
                />
            </a-form-item>
            <span style="font-size: small; color: #ccc" v-if="strategy!=3">优惠金额和优惠折扣选填一个</span>
            <a-form-item label="优惠金额（元）" v-bind="formItemLayout">
                <a-input
                        placeholder="请填写优惠金额"
                        v-decorator="['discountMoney', { rules: [{ required: false, message: '请填写优惠金额' }] }]"
                />
            </a-form-item>
            <a-form-item label="优惠折扣" v-bind="formItemLayout" v-if="strategy != 3">
                <a-input
                        placeholder="请填写优惠折扣(填写0~1的小数)"
                        v-decorator="['discount', { rules: [{ required: false, message: '请填写优惠折扣' }] }]"
                />
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
const moment = require('moment')

export default {
    name: 'addCouponModal',
    data() {
        return {
            formItemLayout: {
                labelCol: {
                    xs: { span: 12 },
                    sm: { span: 6 },
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 },
                },
            },
            strategy : 0,
        }
    },
    computed: {
        ...mapGetters([
            'activeHotelId',
            'addCouponVisible',
        ])
    },
    beforeCreate() {
        // 表单名默认为“form”
        this.form = this.$form.createForm(this, { name: 'addCouponModal' });
    },
    mounted() {

    },
    methods: {
        ...mapMutations([
            'set_addCouponVisible',
            'set_couponVisible'
        ]),
        ...mapActions([
            // addHotelCoupon：添加酒店策略接口
            'addHotelCoupon'
        ]),
        cancel() {
            this.set_addCouponVisible(false)
            this.set_couponVisible(true)
        },
        changeType(v){
            if( v == '3') {
                this.strategy = 3;
            }else if (v == '2'){
                this.strategy = 2;
            }else if(v == '1'){
                this.strategy = 1;
            } else if(v == '4'){
                this.strategy = 4;
            }
            else {
                this.$message.warning('请实现')
            }
        },
        handleSubmit(e) {
            e.preventDefault();
            this.form.validateFieldsAndScroll((err, values) => {
                if (!err) {
                    var st = this.strategy==4? moment(this.form.getFieldValue('time')[0]).format('YYYY-MM-DD HH:mm:ss'): ''
                    var et = this.strategy==4? moment(this.form.getFieldValue('time')[1]).format('YYYY-MM-DD HH:mm:ss'): ''
                    if(this.form.getFieldValue('discount') != undefined){
                        var tmp = Number(this.form.getFieldValue('discount'))
                        if(tmp <0 || tmp > 1){
                            this.$message.warning('折扣值为0到1（含）的小数噢！')
                            return
                        }
                    }
                    const data = {
                        name: this.form.getFieldValue('name'),
                        description: this.form.getFieldValue('description'),
                        type: Number(this.form.getFieldValue('type')),
                        targetMoney: this.strategy==3? Number(this.form.getFieldValue('targetMoney')) : -1,
                        startTime : this.strategy==4? st.substring(0,10) + 'T' + st.substring(11) : '',
                        endTime: this.strategy == 4? et.substring(0,10) + 'T' + et.substring(11): '',
                        discount : this.form.getFieldValue('discount') == undefined? -1 : Number(this.form.getFieldValue('discount')),
                        discountMoney: this.form.getFieldValue('discountMoney')==undefined? -1 : Number(this.form.getFieldValue('discountMoney')),
                        hotelId: this.activeHotelId,
                        bizRegion: this.bizRegion,
                        roomNum: this.strategy==2? Number(this.form.getFieldValue('roomNum')) : -1,
                        status:1,
                    }
                    this.addHotelCoupon(data).then(res=>{
                        this.set_addCouponVisible(false)
                        this.$emit('handle-submit')
                    })
                }
            });
        },
    }
}
</script>