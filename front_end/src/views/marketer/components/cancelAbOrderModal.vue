<template>
    <a-modal
            :visible="cancelAbOrderModalVisible"
            title="撤销异常订单"
            cancelText="取消"
            okText="确定"
            @cancel="cancel"
            @ok="handleSubmit"
    >
        <a-Form :form="form">
            <a-form-item v-bind="formItemLayout" label="恢复信用值比例">
                <a-input-number
                        id="inputNumber"
                        v-model="percent"
                        :min="0"
                        :max="1"
                        :step="0.1"
                        @change="onChange"
                        placeholder="输入0~1的小数,恢复信用值占订单价格的比例"
                />
            </a-form-item >
        </a-Form>
    </a-modal>
</template>
<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    export default {
        name: 'cancelAbOrderModal',
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
                percent:0
            }
        },
        computed: {
            ...mapGetters([
                'cancelAbOrderModalVisible',
            ])
        },
        beforeCreate() {
            this.form = this.$form.createForm(this, { name: 'cancelAbOrderModal' });
        },
        mounted() {

        },
        methods: {
            ...mapMutations([
                'set_cancelAbOrderModalVisible',
            ]),
            ...mapActions([
                'addCredit',
                'annulAbnormalOrder'
            ]),
            cancel() {
                this.set_cancelAbOrderModalVisible(false)
            },
            onChange(credit) {

            },
            handleSubmit(e) {
                e.preventDefault();
                this.form.validateFieldsAndScroll((err, values) => {
                    if (!err) {
                        const data = {
                            orderId: this.record.id,
                            percent: this.percent
                        }
                        console.log(data)
                        this.annulAbnormalOrder(data).then(res=>{
                            this.set_cancelAbOrderModalVisible(false)
                            this.$emit('handle-submit')
                        })
                    }
                });
            },
        }
    }
</script>