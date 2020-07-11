<template>
    <a-modal
            :visible="addCreditModalVisible"
            title="修改信用值"
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
                        { rules: [{required: true, message: '请输入用户邮箱', }], initialValue: record.email }
                    ]"
                />
            </a-form-item>
            <a-form-item v-bind="formItemLayout" label="增加的信用值">
                <a-input
                        v-decorator="[
                        'credit',
                        { rules: [{required: true, message: '请输入要增加的信用值', }] }
                    ]"
                />
            </a-form-item >
        </a-Form>
    </a-modal>
</template>
<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    export default {
        name: 'addCreditModal',
        props: ['record'],
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
                'addCreditModalVisible',
            ])
        },
        beforeCreate() {
            this.form = this.$form.createForm(this, { name: 'addCreditModal' });
        },
        mounted() {

        },
        methods: {
            ...mapMutations([
                'set_addCreditModalVisible',
                'set_addCreditParams',
            ]),
            ...mapActions([
                'getClientList',
                'addCredit',
            ]),
            cancel() {
                this.set_addCreditModalVisible(false)
            },
            handleSubmit(e) {
                e.preventDefault();
                this.form.validateFieldsAndScroll((err, values) => {
                    if (!err) {
                        const data = {
                            email: this.form.getFieldValue('email'),
                            credit: this.form.getFieldValue('credit')
                        }
                        this.set_addCreditParams(data)
                        this.addCredit()
                    }
                });
            },
        }
    }
</script>