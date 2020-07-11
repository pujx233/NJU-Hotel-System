<template>
    <a-modal
        :visible="addHotelModalVisible"
        title="添加酒店"
        cancelText="取消"
        okText="确定"
        @cancel="cancel"
        @ok="handleSubmit"
    >
        <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
            <a-form-item label="酒店名">
                <a-input
                    placeholder="请填写酒店名称"
                    v-decorator="['hotelName', { rules: [{ required: true, message: '请填写酒店名称' }] }]"
                />
            </a-form-item>
            <a-form-item label="酒店地址" v-bind="formItemLayout">
                <a-input
                    placeholder="请填写酒店地址"
                    v-decorator="['address', { rules: [{ required: true, message: '请填写酒店地址' }] }]"
                />
            </a-form-item>
            <a-form-item label="酒店星级" v-bind="formItemLayout">
                <a-select
                    v-decorator="[
                    'hotelStar', 
                    { rules: [{ required: true, message: '请选择酒店星级' }] }]"
                    @change="changeStar"
                >
                  <a-select-option value="三星级">三星级</a-select-option>
                  <a-select-option value="四星级">四星级</a-select-option>
                  <a-select-option value="五星级">五星级</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="商圈" v-bind="formItemLayout">
                <a-select
                        v-decorator="[
                    'bizRegion',
                    { rules: [{ required: true, message: '请选择商圈' }] }]"
                        @change="changeBizRegion"
                >
                    <a-select-option value="西单">西单</a-select-option>
                    <a-select-option value="王府井">王府井</a-select-option>
                    <a-select-option value="大栅栏">大栅栏</a-select-option>
                </a-select>
            </a-form-item>

            <a-form-item label="电话" v-bind="formItemLayout">
                <a-input
                        placeholder="请填写手机号"
                        v-decorator="['phoneNumber', { rules: [{ required: true, message: '请输入手机号' }] }]"
                />
            </a-form-item>
            <a-form-item label="酒店设施" v-bind="formItemLayout">
                <a-select
                        mode="multiple"
                        :default-value="['Wifi']"
                        style="width: 100%"
                        placeholder="至少选择一项"
                        @change="handleChange"
                >
                    <a-select-option v-for="(item) in facilities" :key="item">
                        {{ item }}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="酒店简介" v-bind="formItemLayout">
                <a-input
                    type="textarea"
                    :rows="4"
                    placeholder="请填写酒店简介"
                    v-decorator="['description', { rules: [{ required: true, message: '请填写酒店简介' }] }]"
                />
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
export default {
    name: 'addHotelModal',
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
            facilities: ['WIFI','热水','叫醒服务'],
            hotelService: ''
        }
    },
    computed: {
        ...mapGetters([
            'userId',
            'addHotelParams',
            'addHotelModalVisible'
        ])
    },
    beforeCreate() {
        this.form = this.$form.createForm(this, { name: 'addHotelModal' });
    },
    mounted() {

    },
    methods: {
        ...mapMutations([
            'set_addHotelParams',
            'set_addHotelModalVisible'
        ]),
        ...mapActions([
            'addHotel'
        ]),
        cancel() {
            this.set_addHotelModalVisible(false)
        },
        changeStar(v){

        },
        handleChange(value) {
            this.hotelService = `${value}`;
            console.log(this.hotelService)
        },
        changeBizRegion(v){

        },
        handleSubmit(e) {
            e.preventDefault();
            this.form.validateFieldsAndScroll((err, values) => {
                if (!err) {
                    const data = {
                        adminId: this.userId,
                        name: this.form.getFieldValue('hotelName'),
                        description: this.form.getFieldValue('description'),
                        address: this.form.getFieldValue('address'),
                        phoneNum: this.form.getFieldValue('phoneNumber'),
                        hotelStar: this.form.getFieldValue('hotelStar'),
                        managerId: Number(this.userId),
                        bizRegion: this.form.getFieldValue('bizRegion'),
                        hotelService: this.hotelService,
                    }
                    this.set_addHotelParams(data)
                    this.addHotel()
                }
            });
        },
    }
}
</script>