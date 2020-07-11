<template>
    <a-modal
            :visible="addWebCouponModalVisible"
            title="添加网站优惠策略"
            cancelText="取消"
            okText="确定"
            @cancel="cancel"
            @ok="handleSubmit"
    >
        <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
            <a-form-item label="优惠券类型" v-bind="formItemLayout">
                <a-select
                        v-decorator="[
                    'type',
                    { rules: [{ required: true, message: '请选择优惠券类型' }] }]"
                        @change="changeType"
                >
                    <a-select-option value="1">VIP特定商圈优惠</a-select-option>
<!--                    <a-select-option value="2">多间特惠</a-select-option>-->
<!--                    <a-select-option value="3">满减特惠</a-select-option>-->
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
                        v-decorator="['roomNum', { rules: [{ required: true, message: '请填写达标房间数量' }] }]"
                />
            </a-form-item>
            <a-form-item label="达标VIP等级" v-bind="formItemLayout" v-if="strategy == 1">
                <a-select
                        v-decorator="[
                        'vipLevel',
                        { rules: [{ required: true, message: '请选择达标VIP等级' }] }]"
                        @change="changeVipLevel"
                >
                    <a-select-option value="1">1</a-select-option>
                    <a-select-option value="2">2</a-select-option>
                    <a-select-option value="3">3</a-select-option>
                    <a-select-option value="4">4</a-select-option>
                    <a-select-option value="5">5</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="商圈" v-bind="formItemLayout" v-if="strategy == 1">
                <a-select
                        v-decorator="[
                        'bizRegion',
                        { rules: [{ required: true, message: '请选择商圈' }] }]"
                        @change="changeBizRegion"
                >
                    <a-select-option value="1">西单</a-select-option>
                    <a-select-option value="2">王府井</a-select-option>
                    <a-select-option value="3">大栅栏</a-select-option>
                </a-select>
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
        name: 'addWebCouponModal',
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
                bizRegion: '',
                vipLevel: '',
            }
        },
        computed: {
            ...mapGetters([
                'addWebCouponModalVisible',
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
                'set_addWebCouponModalVisible'
            ]),
            ...mapActions([
                // addHotelCoupon：添加酒店策略接口
                'addHotelCoupon'
            ]),
            cancel() {
                this.set_addWebCouponModalVisible(false)
            },
            changeType(v){
                if( v == '3') {
                    this.strategy = 3;
                }else if (v == '2'){
                    this.strategy = 2;
                }else if(v == '1'){
                    this.strategy = 1;
                }
                else if(v == '4'){
                    this.strategy = 4;
                }
            },
            changeBizRegion(v){
                if( v == '1') {
                    this.bizRegion = '西单';
                }else if (v == '2'){
                    this.bizRegion = '王府井';
                }else if(v == '3'){
                    this.bizRegion = '大栅栏';
                }
                else {
                    this.$message.warning('请实现')
                }
            },
            changeVipLevel(v){
                if( v == '1') {
                    this.vipLevel = '1';
                }else if (v == '2'){
                    this.vipLevel = '2';
                }else if(v == '3'){
                    this.vipLevel = '3';
                }else if (v == '4'){
                    this.vipLevel = '4';
                }else if(v == '5'){
                    this.vipLevel = '5';
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
                            hotelId: -1,
                            bizRegion: this.bizRegion,
                            roomNum: this.strategy==2? Number(this.form.getFieldValue('roomNum')) : -1,
                            status:1,
                            vipLevel: this.vipLevel
                        }
                        console.log(data)
                        this.addHotelCoupon(data).then(res=>{
                            this.set_addWebCouponModalVisible(false)
                            this.$emit('handle-submit')
                        })
                    }
                });
            },
        }
    }
</script>