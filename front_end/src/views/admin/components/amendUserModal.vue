<template>
    <a-modal
            :visible="amendUserModalVisible"
            title="修改信息"
            cancelText="取消"
            okText="确定"
            @cancel="cancel"
            @ok="handleSubmit"
    >
        <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
            <a-form-item label="用户名" >
                <a-input
                        placeholder="请填写用户名"
                        v-decorator="['userName',  { initialValue: record.userName ,rules: [{ required: true, message: '请填写用户名' }] } ]"
                />
            </a-form-item>
            <a-form-item label="手机号" v-bind="formItemLayout">
                <a-input
                        placeholder="请填写手机号"
                        v-decorator="['phoneNumber', { initialValue: record.phoneNumber, rules: [{ required: true, message: '请填写手机号' }] }]"
                />
            </a-form-item>
            <a-form-item label="密码" v-bind="formItemLayout">
                <a-input
                        placeholder="请输入新密码"
                        v-decorator="['password', { initialValue: record.password, rules: [{ required: true, message: '请填写密码' }] }]"
                />
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    export default {
        name: 'amendUserModal',
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
                'userId',
                'addHotelParams',
                'amendUserModalVisible'
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
                'set_amendUserModalVisible'
            ]),
            ...mapActions([
                'updateUserInfo',
                'getManagerList',
                'getClientList',
                'getMarketerList'
            ]),
            cancel() {
                this.set_amendUserModalVisible(false)
            },

            handleSubmit(e) {
                e.preventDefault();
                this.form.validateFieldsAndScroll((err, values) => {
                    if (!err) {
                        console.log(this.record)
                        const data = {
                            id: this.record.id,
                            userName: this.form.getFieldValue('userName'),
                            email: this.record.email,
                            phoneNumber: this.form.getFieldValue('phoneNumber'),
                            credit: this.record.credit,
                            password: this.form.getFieldValue('password'),
                        }
                        this.updateUserInfo(data).then(res=>{
                            this.set_amendUserModalVisible(false)
                            this.$emit('handle-submit',this.record.userType)
                        })
                    }
                });
            },
        }
    }
</script>