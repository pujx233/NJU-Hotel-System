<template>
    <a-modal
            :visible="amendRoomModalVisible"
            title="修改客房信息"
            cancelText="取消"
            okText="确定"
            @cancel="cancel"
            @ok="handleSubmit"
    >
        <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
            <a-form-item label="房型" >
                <span>{{ record.roomType }}</span>
            </a-form-item>
            <a-form-item label="价格" v-bind="formItemLayout">
                <a-input
                        placeholder="请填写价格"
                        v-decorator="['price', { initialValue: record.price, rules: [{ required: true, message: '请填写价格' }] }]"
                />
            </a-form-item>
            <a-form-item label="总数" v-bind="formItemLayout">
                <a-input
                        placeholder="请输入房间总数"
                        v-decorator="['total', { initialValue: record.total, rules: [{ required: true, message: '请填写房间总数' }] }]"
                />
            </a-form-item>
            <a-form-item label="当前数目" v-bind="formItemLayout">
                <a-input
                        placeholder="请输入当前数目"
                        v-decorator="['curNum', { initialValue: record.curNum, rules: [{ required: true, message: '请填写当前房间数' }] }]"
                />
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    export default {
        name: 'amendRoomModal',
        props:['record'],
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
            }
        },
        computed: {
            ...mapGetters([
                'amendRoomModalVisible',
                'activeHotelId',
            ])
        },
        beforeCreate() {
            this.form = this.$form.createForm(this, { name: 'amendRoomModal' });
        },
        mounted() {

        },
        methods: {
            ...mapMutations([
                'set_amendRoomModalVisible',
                'set_roomVisible'
            ]),
            ...mapActions([
                'updateRoomInfo'
            ]),
            cancel() {
                this.set_amendRoomModalVisible(false)
                this.set_roomVisible(true)
            },

            handleSubmit(e) {
                e.preventDefault();
                this.form.validateFieldsAndScroll((err, values) => {
                    if (!err) {
                        console.log("提交修改房间",this.record)
                        const data = {
                            id: this.record.id,
                            hotelId: this.activeHotelId,
                            roomType: this.record.roomType == '大床房'? 'BigBed' : (this.record.roomType == '双床房' ? 'DoubleBed' : 'Family'),
                            curNum: this.form.getFieldValue('curNum'),
                            price: this.form.getFieldValue('price'),
                            total: this.form.getFieldValue('total'),
                        }
                        this.updateRoomInfo(data).then(res=>{
                            this.set_amendRoomModalVisible(false)
                            this.set_roomVisible(true)
                            this.$emit('handle-submit')
                        })
                    }
                });
            },
        }
    }
</script>