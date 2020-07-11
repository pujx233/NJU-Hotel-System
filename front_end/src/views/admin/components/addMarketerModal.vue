<template>
    <a-modal
            :visible="addMarketerModalVisible"
            title="注册网站营销人员账号"
            cancelText="取消"
            okText="确定"
            @cancel="cancel"
            @ok="handleSubmit"
    >
        <a-Form :form="form">
            <a-form-item v-bind="formItemLayout" label="用户邮箱">
                <a-input
                        v-decorator="[
                        'email',
                        { rules: [{required: true, message: '请输入用户邮箱', }] }
                    ]"
                />
            </a-form-item>
            <a-form-item v-bind="formItemLayout" label="密码">
                <a-input
                        v-decorator="[
                        'password',
                        { rules: [{required: true, message: '请输入密码', }] }
                    ]"
                />
            </a-form-item >
        </a-Form>
    </a-modal>
</template>
<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    export default {
        name: 'addMarketerModal',
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
                'addMarketerModalVisible',
                'marketerList',
            ])
        },
        beforeCreate() {
            this.form = this.$form.createForm(this, { name: 'addManagerModal' });
        },
        mounted() {

        },
        methods: {
            ...mapMutations([
                'set_addMarketerModalVisible',
                'set_addMarketerParams',
            ]),
            ...mapActions([
                'getMarketerList',
                'addMarketer',
            ]),
            cancel() {
                this.set_addMarketerModalVisible(false)
            },
            handleSubmit(e) {
                e.preventDefault();
                this.form.validateFieldsAndScroll((err, values) => {
                    if (!err) {
                        const data = {
                            email: this.form.getFieldValue('email'),
                            password: this.form.getFieldValue('password')
                        }
                        this.set_addMarketerParams(data)
                        this.addMarketer()
                    }
                });
            },
        }
    }
</script>